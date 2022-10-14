package jjfactory.webclient.business.category.dto.res;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CategoryNameRes {
    private String name;

    public CategoryNameRes(String name) {
        this.name = name;
    }
}
