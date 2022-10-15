package jjfactory.webclient.business.post.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostImageCreate {
    private String saveName;
    private String imgUrl;
    private String originName;

    @Builder
    public PostImageCreate(String saveName, String imgUrl, String originName) {
        this.saveName = saveName;
        this.imgUrl = imgUrl;
        this.originName = originName;
    }
}
