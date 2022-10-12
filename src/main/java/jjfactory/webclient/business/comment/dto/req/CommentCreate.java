package jjfactory.webclient.business.comment.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommentCreate {
    private Long boardId;
    private Long parentId;
    private String content;

    @Builder
    public CommentCreate(Long boardId, Long parentId, String content) {
        this.boardId = boardId;
        this.parentId = parentId;
        this.content = content;
    }
}
