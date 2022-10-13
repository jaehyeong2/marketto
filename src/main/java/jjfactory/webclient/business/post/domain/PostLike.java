package jjfactory.webclient.business.post.domain;

import jjfactory.webclient.business.BaseEntity;
import jjfactory.webclient.business.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PostLike extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Builder
    public PostLike(Member member, Post post) {
        this.member = member;
        this.post = post;
    }

    public static PostLike create(Member member,Post post){
        post.increaseLikeCount();

        return PostLike.builder()
                .member(member)
                .post(post)
                .build();
    }
}
