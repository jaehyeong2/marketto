package jjfactory.webclient.business.post.dto.res;

import jjfactory.webclient.business.post.domain.PostImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostImageRes {
    private String imagePath;

    @Builder
    public PostImageRes(PostImage image) {
        this.imagePath = image.getImgUrl();
    }
}
