package jjfactory.webclient.business.post.dto.res;


import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.post.domain.Post;
import jjfactory.webclient.business.post.domain.PostImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class PostDetailRes {
    private String title;
    private String content;
    private String username;
    private String createdAt;
    private int viewCount;
    private int likeCount;
    private List<String> images;


    @Builder
    public PostDetailRes(String title,String content, String username, String createdAt, int viewCount, int likeCount,List<String> images) {
        this.title = title;
        this.content = content;
        this.username = username;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.images = images;
    }

    public PostDetailRes(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.images = post.getImages().stream().map(PostImage::getUrl)
                .collect(Collectors.toList());
        this.username = post.getMember().getUsername();
        this.viewCount = post.getViewCount();
        this.likeCount = post.getLikeCount();
        this.createdAt = post.getCreatedAt()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public PostDetailRes(Post post, Member member) {
        this.title = post.getTitle();
        this.username = member.getUsername();
        this.content = post.getContent();
        this.images = post.getImages().stream().map(PostImage::getUrl)
                .collect(Collectors.toList());
        this.viewCount = post.getViewCount();
        this.createdAt = post.getCreatedAt()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
