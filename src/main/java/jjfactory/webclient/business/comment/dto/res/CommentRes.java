package jjfactory.webclient.business.comment.dto.res;

import jjfactory.webclient.business.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class CommentRes {
    private Long commentId;
    private String username;
    private String content;
    private String createDate;
    private List<CommentRes> child = new ArrayList<>();

    @Builder
    public CommentRes(Long commentId, String username, String content, String createDate, List<CommentRes> child) {
        this.commentId = commentId;
        this.username = username;
        this.content = content;
        this.createDate = createDate;
        this.child = child;
    }

    public CommentRes(Comment comment) {
        this.commentId = comment.getId();
        this.username = comment.getMember().getUsername();
        this.content = comment.getContent();
        this.createDate = comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.child = comment.getChild().stream().map(CommentRes::new).collect(Collectors.toList());
    }
}
