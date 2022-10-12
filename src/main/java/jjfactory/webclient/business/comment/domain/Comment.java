package jjfactory.webclient.business.comment.domain;

import jjfactory.webclient.business.BaseEntity;
import jjfactory.webclient.business.comment.dto.req.CommentCreate;
import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.post.domain.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> child = new ArrayList<>();

    @Builder
    public Comment(Member member, Post post, String content, Comment parent, List<Comment> child) {
        this.member = member;
        this.post = post;
        this.content = content;
        this.parent = parent;
        this.child = child;
    }

    public static Comment create(CommentCreate dto,Member member,Post post){
        return Comment.builder()
                .post(post)
                .member(member)
                .content(dto.getContent())
                .build();
    }

    public void modify(String content) {
        this.content = content;
    }

    public void addParentComment(Comment parent){
        this.parent = parent;
        parent.child.add(this);
    }

    private void addParent(Comment parent) {
        this.parent = parent;
    }

}
