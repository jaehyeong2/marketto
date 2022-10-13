package jjfactory.webclient.business.post.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostUpdate {
    private String title;
    private String content;
    private int price;

    @Builder
    public PostUpdate(String title, String content, int price) {
        this.title = title;
        this.content = content;
        this.price = price;
    }
}
