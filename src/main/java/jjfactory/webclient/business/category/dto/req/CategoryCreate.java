package jjfactory.webclient.business.category.dto.req;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CategoryCreate {
    private String name;

    public CategoryCreate(String name) {
        this.name = name;
    }
}
