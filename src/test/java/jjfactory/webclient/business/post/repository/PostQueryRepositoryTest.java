package jjfactory.webclient.business.post.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.post.domain.Post;
import jjfactory.webclient.business.post.dto.res.PostRes;
import jjfactory.webclient.global.dto.req.MyPageReq;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.format.DateTimeFormatter.ofPattern;
import static jjfactory.webclient.business.post.domain.QPost.post;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class PostQueryRepositoryTest {
    @Autowired
    EntityManager em;
    private JPAQueryFactory queryFactory;

    @BeforeEach
    void init(){
        queryFactory = new JPAQueryFactory(em);

        Member wogud222 = Member.builder().username("wogud222")
                .build();
        em.persist(wogud222);

        for (int i = 1; i < 16; i++) {
            Post post = Post.builder().member(wogud222)
                    .content("하이" + i)
                    .title("제목" + (i+1))
                    .build();

            em.persist(post);
        }
    }

    @Test
    @DisplayName("모든 게시물 조회 성공 + 날짜 검색 + 문자검색")
    void findAllPosts() {
        //given
        String startDate = "2022-09-01 00:00:00";
        String endDate = "2022-10-30 23:59:59";

        PageRequest pageable = new MyPageReq(1, 10).of();

        //when
        List<PostRes> posts = queryFactory.select(Projections.constructor(PostRes.class, post))
                .from(post)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(between(startDate, endDate),
                        contains("12"))
                .orderBy(post.createdAt.desc())
                .fetch();

        int total = queryFactory.select(Projections.constructor(PostRes.class, post))
                .from(post)
                .where(between(startDate, endDate),
                        contains("12"))
                .fetch().size();
        //then
        assertThat(total).isEqualTo(2);
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