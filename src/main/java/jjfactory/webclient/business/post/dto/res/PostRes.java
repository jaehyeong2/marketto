package jjfactory.webclient.business.post.dto.res;


import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.post.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
public class PostRes {
    private String title;
    private String username;
    private String createdAt;
    private int viewCount;
    private int likeCount;
    @Builder
    public PostRes(String title, String username, String createdAt, int viewCount,int likeCount) {
        this.title = title;
        this.username = username;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }

    public PostRes(Post post) {
        this.title = post.getTitle();
        this.username = post.getMember().getUsername();
        this.viewCount = post.getViewCount();
        this.likeCount = post.getLikeCount();
        this.createdAt = post.getCreatedAt()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public PostRes(Post post, Member member) {
        this.title = post.getTitle();
        this.username = member.getUsername();
        this.viewCount = post.getViewCount();
        this.createdAt = post.getCreatedAt()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
