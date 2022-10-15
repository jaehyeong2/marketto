package jjfactory.webclient.business.post.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class PostCreate {
    @NotNull
    private Long categoryId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private int price;

    @Builder
    public PostCreate(Long categoryId, String title, String content, int price) {
        this.categoryId = categoryId;
        this.title = title;
        this.content = content;
        this.price = price;
    }
}
