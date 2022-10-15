package jjfactory.webclient.business.post.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class PostUpdate {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private int price;

    @Builder
    public PostUpdate(String title, String content, int price) {
        this.title = title;
        this.content = content;
        this.price = price;
    }
}
