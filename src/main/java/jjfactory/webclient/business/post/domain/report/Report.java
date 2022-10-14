package jjfactory.webclient.business.post.domain.report;

import jjfactory.webclient.business.BaseEntity;
import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.post.domain.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Report extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "reporter_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Enumerated(EnumType.STRING)
    private ReportReason reason;

    @Builder
    public Report(Member member, Post post, ReportReason reason) {
        this.member = member;
        this.post = post;
        this.reason = reason;
    }

    public static Report create(Member member,Post post,ReportReason reason){
        post.increaseReportCount();

        return Report.builder()
                .member(member)
                .post(post)
                .reason(reason)
                .build();
    }

}
