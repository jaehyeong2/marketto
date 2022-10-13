package jjfactory.webclient.business.comment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.webclient.business.comment.domain.QComment;
import jjfactory.webclient.business.comment.dto.res.CommentRes;
import jjfactory.webclient.business.comment.dto.res.MyCommentRes;
import jjfactory.webclient.business.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static jjfactory.webclient.business.comment.domain.QComment.*;

@RequiredArgsConstructor
@Repository
public class CommentQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<CommentRes> findComments(Pageable pageable,Long postId){
        List<CommentRes> comments = queryFactory.select(Projections.constructor(CommentRes.class, comment))
                .from(comment)
                .where(comment.post.id.eq(postId),comment.parent.id.isNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int total = queryFactory.select(Projections.constructor(CommentRes.class, comment))
                .from(comment)
                .where(comment.post.id.eq(postId),comment.parent.id.isNull())
                .fetch().size();

        return new PageImpl<>(comments,pageable,total);
    }

    public Page<MyCommentRes> findMyComments(Pageable pageable, Member member){
        List<MyCommentRes> comments = queryFactory.select(Projections.constructor(MyCommentRes.class, comment))
                .from(comment)
                .where(comment.member.eq(member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int total = queryFactory.select(Projections.constructor(MyCommentRes.class, comment))
                .from(comment)
                .where(comment.member.eq(member))
                .fetch().size();

        return new PageImpl<>(comments,pageable,total);
    }
}
