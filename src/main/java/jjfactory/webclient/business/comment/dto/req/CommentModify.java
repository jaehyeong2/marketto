package jjfactory.webclient.business.comment.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class CommentModify {
    @NotBlank
    private String content;
    @Builder
    public CommentModify(String content) {
        this.content = content;
    }
}
