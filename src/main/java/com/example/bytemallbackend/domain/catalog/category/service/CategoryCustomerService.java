package com.example.bytemallbackend.domain.catalog.category.service;

import com.example.bytemallbackend.domain.catalog.category.dto.response.CategoryHierarchyResponse;
import com.example.bytemallbackend.domain.catalog.category.entity.Category;
import com.example.bytemallbackend.domain.catalog.category.entity.enumerate.RootCategory;
import com.example.bytemallbackend.domain.catalog.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryCustomerService {

    private final CategoryRepository categoryRepository;

    /**
     * [일반 유저용] 전체 카테고리 계층 구조 조회
     */
    public Map<String, List<CategoryHierarchyResponse>> getAllCategoriesForUser() {
        // 1. 전체 데이터 조회
        /**
         * Depth가 낮고 Order가 빠른 순서대로 리스트가 생성
         * ID	Name	      RootCategory	Parent ID	Depth
         * 100	여성의류	       FASHION	     NULL	     1
         * 200	가전/디지털	   ELECTRONICS 	 NULL	     1
         * 101	티셔츠          FASHION	     100	     2
         * 102	원피스	       FASHION	     100	     2
         */
        List<Category> allCategories = categoryRepository.findAllOrderByDepthAndDisplayOrder();

        /**
         * dtoMap 상태:
         *  100L : {name: "여성의류", children: []}
         *  200L : {name: "가전/디지털", children: []}
         *  101L : {name: "티셔츠", children: []}
         *  102L : {name: "원피스", children: []}
         */
        // 2. DTO 변환 (Map에 저장)
        Map<Long, CategoryHierarchyResponse> dtoMap = new LinkedHashMap<>();
        for (Category category : allCategories) {
            dtoMap.put(category.getId(), CategoryHierarchyResponse.from(category));
        }

        /**
         * Enum에 정의된 모든 RootCategory를 Key로 미리 등록한다. (내용물은 빈 리스트)
         * ㄴ resultMap: {"FASHION": [], "ELECTRONICS": [], "BEAUTY": [], ...}
         */
        // 3. 트리 조립 및 Root 그룹핑
        Map<String, List<CategoryHierarchyResponse>> resultMap = new LinkedHashMap<>();
        // Enum 순서대로 미리 Map 초기화 (안 해도 되지만 순서 보장을 위해)
        for (RootCategory rc : RootCategory.values()) {
            resultMap.put(rc.name(), new ArrayList<>());
        }

        /**
         * 리스트를 다시 순회하며, 각 DTO가 "루트인지 아니면 누군가의 자식인지" 판별하여 제자리를 찾아준다.
         * 1. ID 100 (여성의류): cat.getParent() == null이다.
         *      ㄴ resultMap.get("FASHION") 리스트에 추가한다.
         * 2. ID 200 (가전/디지털): cat.getParent() == null이다.
         *      ㄴ resultMap.get("ELECTRONICS") 리스트에 추가한다.
         * 3. ID 101 (티셔츠): 부모 ID가 100L이다.
         *      ㄴ dtoMap.get(100L)을 찾아 "여성의류" DTO를 가져온다.
         *      ㄴ 여성의류 DTO의 addChild(티셔츠 DTO)를 실행한다. (중첩 시작)
         * 4. ID 102 (원피스): 부모 ID가 100L이다.
         *      ㄴ dtoMap.get(100L)을 찾아 "여성의류" DTO의 addChild(원피스 DTO)를 실행한다.
         */
        for (Category category : allCategories) {
            CategoryHierarchyResponse currentDto = dtoMap.get(category.getId());

            if (category.getParent() == null) {
                // 1뎁스(최상위)라면 결과 맵(RootCategory 그룹)에 바로 추가
                resultMap.get(category.getRootCategory().name()).add(currentDto);
            } else {
                // 하위 뎁스라면 부모 DTO를 찾아 자식으로 연결
                CategoryHierarchyResponse parentDto = dtoMap.get(category.getParent().getId());
                if (parentDto != null) {
                    parentDto.addChild(currentDto);
                }
            }
        }

        return resultMap;
        /**
         * {
         *   "FASHION": [
         *     {
         *       "id": 100,
         *       "name": "여성의류",
         *       "children": [
         *         { "id": 101, "name": "티셔츠", "children": [] },
         *         { "id": 102, "name": "원피스", "children": [] }
         *       ]
         *     }
         *   ],
         *   "ELECTRONICS": [
         *     {
         *       "id": 200,
         *       "name": "가전/디지털",
         *       "children": []
         *     }
         *   ]
         * }
         */
    }
}
