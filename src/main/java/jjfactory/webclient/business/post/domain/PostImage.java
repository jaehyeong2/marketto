package jjfactory.webclient.business.post.domain;

import jjfactory.webclient.business.BaseEntity;
import jjfactory.webclient.business.post.dto.req.PostImageCreate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PostImage extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String saveName;

    @Column(nullable = false)
    private String imgUrl;

    @Builder
    public PostImage(Post post, String originName, String saveName, String imgUrl) {
        this.post = post;
        this.originName = originName;
        this.saveName = saveName;
        this.imgUrl = imgUrl;
    }

    public static PostImage create(PostImageCreate dto, Post post){
        PostImage image = PostImage.builder()
                .post(post)
                .saveName(dto.getSaveName())
                .originName(dto.getOriginName())
                .imgUrl(dto.getImgUrl())
                .build();

        post.addImage(image);
        return image;
    }
}
