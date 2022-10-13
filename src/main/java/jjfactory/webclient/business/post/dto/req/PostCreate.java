package jjfactory.webclient.business.post.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostCreate {
    private Long categoryId;
    private String title;
    private String content;
    private int price;

    public PostCreate(Long categoryId, String title, String content, int price) {
        this.categoryId = categoryId;
        this.title = title;
        this.content = content;
        this.price = price;
    }
}
