package jjfactory.webclient.business.post.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.post.domain.QPost;
import jjfactory.webclient.business.post.dto.res.PostDetailRes;
import jjfactory.webclient.business.post.dto.res.PostRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.format.DateTimeFormatter.*;
import static jjfactory.webclient.business.post.domain.QPost.*;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {
    private final JPAQueryFactory queryFactory;

    public PostDetailRes findPost(Long postId){
        return queryFactory.select(Projections.constructor(PostDetailRes.class, post))
                .from(post)
                .where(post.id.eq(postId))
                .fetchOne();
    }

    public Page<PostRes> findMyPosts(Pageable pageable, String startDate, String endDate, Member loginMember){
        List<PostRes> posts = queryFactory.select(Projections.constructor(PostRes.class, post))
                .from(post)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(between(startDate, endDate),
                        post.member.eq(loginMember))
                .orderBy(post.createdAt.desc())
                .fetch();

        int total = queryFactory.select(Projections.constructor(PostRes.class, post))
                .from(post)
                .where(between(startDate, endDate),
                        post.member.eq(loginMember))
                .fetch().size();

        return new PageImpl<>(posts,pageable,total);
    }

    public Page<PostRes> findAllPosts(Pageable pageable,String startDate,String endDate,String query,Boolean orderType){
        List<PostRes> posts = queryFactory.select(Projections.constructor(PostRes.class, post))
                .from(post)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(between(startDate, endDate),
                        contains(query))
                .orderBy(orderBy(orderType))
                .fetch();

        int total = queryFactory.select(Projections.constructor(PostRes.class, post))
                .from(post)
                .where(between(startDate, endDate),
                        contains(query))
                .fetch().size();

        return new PageImpl<>(posts,pageable,total);
    }

    private OrderSpecifier<?> orderBy(Boolean orderType) {
        if(orderType == null) return post.createdAt.desc();
        return post.viewCount.desc();
    }

    public List<PostRes> findPopularPosts(){
        return queryFactory.select(Projections.constructor(PostRes.class, post))
                .from(post)
                .orderBy(post.likeCount.desc())
                .limit(10)
                .fetch();
    }

    private BooleanExpression contains(String query) {
        if(!StringUtils.hasText(query)) return post.id.isNotNull();

        return post.content.contains(query)
                .or(post.title.contains(query))
                .or(post.member.username.contains(query));
    }

    private BooleanExpression between(String startDate, String endDate) {
        if(!StringUtils.hasText(startDate) && !StringUtils.hasText(endDate)) return post.id.isNotNull();

        LocalDateTime start = LocalDateTime.parse(startDate, ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime end = LocalDateTime.parse(endDate, ofPattern("yyyy-MM-dd HH:mm:ss"));
        return post.createdAt.between(start, end);
    }

}
