package jjfactory.webclient.business.comment.dto.res;

import jjfactory.webclient.business.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MyCommentRes {
    private String postTitle;
    private String content;

    @Builder
    public MyCommentRes(String postTitle, String content) {
        this.postTitle = postTitle;
        this.content = content;
    }

    public MyCommentRes(Comment comment) {
        this.postTitle = comment.getPost().getTitle();
        this.content = comment.getContent();
    }
}
