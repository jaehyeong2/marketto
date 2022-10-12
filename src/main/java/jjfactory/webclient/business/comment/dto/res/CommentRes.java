package jjfactory.webclient.business.comment.dto.res;

import jjfactory.webclient.business.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
public class CommentRes {
    private Long commentId;
    private String username;
    private String content;
    private String createDate;

    @Builder
    public CommentRes(Long commentId, String username, String content, String createDate) {
        this.commentId = commentId;
        this.username = username;
        this.content = content;
        this.createDate = createDate;
    }

    public CommentRes(Comment comment) {
        this.commentId = comment.getId();
        this.username = comment.getMember().getUsername();
        this.content = comment.getContent();
        this.createDate = comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
