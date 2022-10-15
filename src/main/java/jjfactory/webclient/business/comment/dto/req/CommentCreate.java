package jjfactory.webclient.business.comment.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class CommentCreate {
    @NotNull
    private Long boardId;
    private Long parentId;
    @NotBlank
    private String content;

    @Builder
    public CommentCreate(Long boardId, Long parentId, String content) {
        this.boardId = boardId;
        this.parentId = parentId;
        this.content = content;
    }
}
