package com.example.bytemallbackend.domain.catalog.category.service;

import com.example.bytemallbackend.domain.catalog.category.dto.request.CategoryCreateRequest;
import com.example.bytemallbackend.domain.catalog.category.dto.request.CategoryUpdateRequest;
import com.example.bytemallbackend.domain.catalog.category.dto.response.CategoryResponse;
import com.example.bytemallbackend.domain.catalog.category.entity.Category;
import com.example.bytemallbackend.domain.catalog.category.entity.enumerate.RootCategory;
import com.example.bytemallbackend.domain.catalog.category.exception.CategoryErrorCode;
import com.example.bytemallbackend.domain.catalog.category.repository.CategoryRepository;
import com.example.bytemallbackend.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryAdminService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 생성
     */
    @Transactional
    public Long createCategory(CategoryCreateRequest request) {

        Category parentCategory = null;
        Integer depth = 1; // 최상위 카테고리를 만들 수도 있기 때문에 기본값을 1로 설정

        // 1.부모 카테고리 조회 및 Depth 설정
        if (request.getParentId() != null) {
            parentCategory = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new BusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));
            depth = parentCategory.getDepth() + 1;
        }

        // 2. 순서(DisplayOrder) 계산 로직 (카테고리 생성 시 강제적으로 마지막 순서로 지정)
        Integer maxDisplayOrder;
        if (parentCategory != null) {
            // 하위 카테고리인 경우: 부모가 같은 형제 카테고리들의 마지막(Max) displayOrder
            maxDisplayOrder = categoryRepository.findMaxDisplayOrderByParent(parentCategory);
        } else {
            // 최상위 카테고리인 경우: 대분류(Root) 바로 하위 카테고리들의 마지막(Max) displayOrder
            maxDisplayOrder = categoryRepository.findMaxDisplayOrderByRootCategory(request.getRootCategory());
        }
        // 형제가 없어서 null이 반환되면 1번, 아니면 max + 1
        int nextDisplayOrder = (maxDisplayOrder == null) ? 1 : maxDisplayOrder + 1;

        // 3. 엔티티 생성
        Category newCategory = Category.of(
                request.getName(),
                request.getRootCategory(),
                parentCategory,
                depth,
                nextDisplayOrder
        );

        // 4. 연관관계 설정 (부모가 존재할 경우에만 수행)
        if (parentCategory != null) {
            parentCategory.assignChild(newCategory);
        }

        // 5. DB 1차 저장 (Id를 가져오기 위햬)
        Long newCategoryId = categoryRepository.save(newCategory).getId();

        // 6. Path 구성 및 업데이트
        String newPath;
        if (parentCategory != null) { // 하위 카테고리
            newPath = parentCategory.getPath() + "/" + newCategoryId;
        } else { // 최상위 카테고리
            newPath = String.valueOf(newCategoryId);
        }

        newCategory.updatePath(newPath);

        return newCategoryId;
    }

    /**
     * 카테고리 일괄 수정 (이름 변경 + 순서 변경 동시 처리)
     * ㄴ [변경 사항 저장] 버튼 클릭 시 호출
     * ㄴ 프론트엔드에서 특정 RootCategory 하위에 있는 모든 카테고리 정보를 CategoryUpdateRequest로 변환하여 보내준다는 전제하에 작성된 코드
     */
    @Transactional
    public void updateCategoriesBatch(List<CategoryUpdateRequest> requests) {

        // 1. 요청 들어온 ID 목록 추출
        List<Long> categoryIds = requests.stream()
                .map(CategoryUpdateRequest::getCategoryId)
                .toList();

        // 2. ID 목록으로 엔티티들을 한 번에 조회
        List<Category> categories = categoryRepository.findAllById(categoryIds);

        // 3. 조회를 편하게 하기 위해 Map으로 변환 (Key: ID, Value: Entity)
        Map<Long, Category> categoryMap = categories.stream()
                .collect(Collectors.toMap(Category::getId, c -> c));

        // 4. 요청 리스트를 순회하며 데이터 업데이트
        for (CategoryUpdateRequest request : requests) {
            Category category = categoryMap.get(request.getCategoryId());

            if (category != null) {
                // 4-1. 이름 변경 (값이 있을 때만)
                if (request.getName() != null && !request.getName().isBlank()) {
                    category.changeName(request.getName());
                }
                // 4-2. 순서 변경 (프론트에서 계산해서 보낸 값 그대로 적용)
                category.changeDisplayOrder(request.getDisplayOrder());
            }
        }
    }

    /**
     * 특정 RootCategory 하위의 모든 카테고리를 계층 구조 순서(DFS)로 정렬하여 조회
     */
    public List<CategoryResponse> getCategoriesByRoot(RootCategory rootCategory) {
        // 1. 해당 루트 카테고리의 모든 데이터 조회
        /**
         * DB에서 가져온 Raw 데이터 (allCategories)
         * ㄴ (ID:1, Name:상의, Parent:null, Order:1)
         * ㄴ (ID:2, Name:하의, Parent:null, Order:2)
         * ㄴ (ID:3, Name:반팔, Parent:1, Order:2)
         * ㄴ (ID:4, Name:셔츠, Parent:1, Order:1)
         */
        List<Category> allCategories = categoryRepository.findAllByRootCategory(rootCategory);

        // 2. 부모 ID를 기준으로 그룹핑 (Key: parentId, Value: 자식 리스트)
        // ㄴ 최상위 카테고리의 parent는 null이므로 Key가 null인 곳에 모임
        /**
         * 실행 후 (categoriesByParent 맵 생성)
         * ㄴ Key: 0L -> Value: [상의, 하의] < parent가 null인 상의, 하의는 Key가 0
         * ㄴ Key: 1L -> Value: [반팔, 셔츠]
         * ㄴ Key: 2L -> Value: [ ] (없음) -> 아래 Sort 시에 null이므로 이전 단계로 회귀 예정
         */
        Map<Long, List<Category>> categoriesByParent = allCategories.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getParent() != null ? c.getParent().getId() : 0L // 0L은 임시 Root Key
                ));

        // 3. 재귀적으로 리스트를 순서대로 쌓을 결과 리스트
        List<CategoryResponse> result = new ArrayList<>();

        // 4. 최상위 카테고리(부모가 null인 애들)부터 시작하여 DFS 탐색
        // ㄴ 편의상 parentId가 없는 경우를 key 0L로 처리했으므로 0L부터 시작
        sortCategoriesRecursive(0L, categoriesByParent, result);

        return result;
    }
    
    /**
     * sortCategoriesRecursive 재귀 탐색
     * ㄴ 1. 호출: sort(0L, ...) (최상위 탐색 시작)
     *      ㄴ 0L의 자식 [상의, 하의]를 Order순 정렬 -> [상의, 하의]
     *      ㄴ 상의(ID:1)를 result에 추가. -> [상의]
     *      ㄴ 재귀 호출: sort(1L, ...) (상의의 자식 탐색)
     *          ㄴ 1L의 자식 [반팔, 셔츠]를 Order순 정렬 -> [셔츠, 반팔]
     *          ㄴ 셔츠(ID:4) 추가 -> [상의, 셔츠]
     *          ㄴ 반팔(ID:3) 추가 -> [상의, 셔츠, 반팔]
     * ㄴ 2. 다시 0L로 돌아옴
     *      ㄴ 하의(ID:2)를 result에 추가. -> [상의, 셔츠, 반팔, 하의]
     *
     * [최종 결과]: [상의, 셔츠, 반팔, 하의] 순서로 정렬된 리스트 반환.
     */
    private void sortCategoriesRecursive(Long parentId, Map<Long, List<Category>> map, List<CategoryResponse> result) {
        List<Category> children = map.get(parentId);

        if (children == null) return; // 자식이 없으면 종료

        // displayOrder 순으로 정렬
        children.sort(Comparator.comparingInt(Category::getDisplayOrder));

        for (Category child : children) {
            // 1. 부모 추가
            result.add(CategoryResponse.from(child));

            // 2. 이 자식의 자식들을 찾아 바로 밑에 추가 (재귀)
            sortCategoriesRecursive(child.getId(), map, result);
        }
    }
    
    /**
     * 카테고리 삭제
     * ㄴ 하위 카테고리까지 모두 삭제됨 (CascadeType.ALL)
     * ㄴ 단, 상품이 연결되어 있다면 예외 발생 (FK 제약조건)
     */
    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        // TODO: 상품 연결되어 있는 부분 예외 처리 해줘야 함
        categoryRepository.delete(category);
    }

}
