package jjfactory.webclient.business.category.domain;

import jjfactory.webclient.business.BaseEntity;
import jjfactory.webclient.business.category.dto.req.CategoryCreate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Category extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Builder
    public Category(String name) {
        this.name = name;
    }

    public static Category create(CategoryCreate dto){
        return Category.builder()
                .name(dto.getName())
                .build();
    }
}
