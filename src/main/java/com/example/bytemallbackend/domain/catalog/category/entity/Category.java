package com.example.bytemallbackend.domain.catalog.category.entity;

import com.example.bytemallbackend.domain.catalog.category.entity.enumerate.RootCategory;
import com.example.bytemallbackend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
public class Category extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    // 카테고리 이름 (예: 하의, 티셔츠, ...)
    @Column(nullable = false)
    private String name;

    // Enum (최상위 카테고리)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RootCategory rootCategory;

    // parent_id = FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children = new ArrayList<>();

    // 카테고리 별 하위 경로 (1/10/22)
    @Column(nullable = false)
    private String path;

    // UI 계층 표현 및 필터링용 깊이 (1, 2, 3, ...)
    @Column(nullable = false)
    private Integer depth;

    @Column(nullable = false)
    private Integer displayOrder;

    /**
     * 생성자
     */
    protected Category() { }

    private Category(String name, RootCategory rootCategory, Category parent, Integer depth, Integer displayOrder) {
        this.name = name;
        this.rootCategory = rootCategory;
        this.parent = parent;
        this.path = "";
        this.depth = depth;
        this.displayOrder = displayOrder;
    }

    public static Category of(String name, RootCategory rootCategory, Category parent, Integer depth, Integer displayOrder) {
        return new Category(name, rootCategory, parent, depth, displayOrder);
    }

    // == 연관관계 편의 메서드 == //
    public void addChildren(Category child) { // 호출 주체가 request가 아님(그 request의 부모가 호출한다는 전제하에 만든 편의 메서드임)
        this.children.add(child);
        child.parent = this;
    }

    //== 비즈니스 로직 ==//

    // 카테고리 이름 변경 메서드
    public void changeName(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
    }

    // 카테고리 경로 변경 메서드
    public void updatePath(String path) {
        this.path = path;
    }

    // 디스플레이 순서 변경 메서드
    public void changeDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

}
