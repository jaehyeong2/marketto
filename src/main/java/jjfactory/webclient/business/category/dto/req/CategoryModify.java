package jjfactory.webclient.business.category.dto.req;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CategoryModify {
    private String name;

    public CategoryModify(String name) {
        this.name = name;
    }
}
