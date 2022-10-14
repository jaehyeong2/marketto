package jjfactory.webclient.business.post.dto.req;

import jjfactory.webclient.business.post.domain.report.ReportReason;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReportCreate {
    private Long postId;
    private ReportReason reason;

    @Builder
    public ReportCreate(Long postId, ReportReason reason) {
        this.postId = postId;
        this.reason = reason;
    }
}
