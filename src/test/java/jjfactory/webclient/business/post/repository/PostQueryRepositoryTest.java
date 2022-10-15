package jjfactory.webclient.business.post.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.post.domain.Post;
import jjfactory.webclient.business.post.domain.PostImage;
import jjfactory.webclient.business.post.domain.QPost;
import jjfactory.webclient.business.post.dto.req.PostImageCreate;
import jjfactory.webclient.business.post.dto.res.PostDetailRes;
import jjfactory.webclient.business.post.dto.res.PostRes;
import jjfactory.webclient.global.config.QueryDslConfig;
import jjfactory.webclient.global.dto.req.MyPageReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static java.time.format.DateTimeFormatter.ofPattern;
import static jjfactory.webclient.business.member.domain.QMember.*;
import static jjfactory.webclient.business.post.domain.QPost.post;
import static org.assertj.core.api.Assertions.*;

@Import(QueryDslConfig.class)
@DataJpaTest
class PostQueryRepositoryTest {
    @Autowired
    EntityManager em;
    private JPAQueryFactory queryFactory;
    private Member wogud222;

    @BeforeEach
    void init(){
        queryFactory = new JPAQueryFactory(em);

        wogud222 = Member.builder().username("wogud222")
                .build();
        em.persist(wogud222);

        Member wogud333 = Member.builder().username("wogud333")
                .build();
        em.persist(wogud333);

        for (int i = 1; i < 51; i++) {
            Member memberTmp = wogud222;
            if(i %2 == 0) memberTmp = wogud333;

            Post post = Post.builder().member(memberTmp)
                    .content("하이" + i)
                    .title("제목" + (i+1))
                    .likeCount(i)
                    .viewCount(i)
                    .build();

            em.persist(post);
        }
    }

    @Test
    @DisplayName("게시물 단건 조회 성공")
    void findPost(){
        //given
        Post findPost = Post.builder()
                .member(wogud222)
                .build();

        em.persist(findPost);

        PostImageCreate imageDto = PostImageCreate.builder()
                .originName("/test/txt")
                .saveName("testName")
                .imgUrl("testurl").build();

        PostImage postImage = PostImage.create(imageDto, findPost);
        em.persist(postImage);

        //when
        PostDetailRes res = queryFactory.select(Projections.constructor(PostDetailRes.class, QPost.post))
                .from(QPost.post)
                .where(QPost.post.id.eq(findPost.getId()))
                .fetchOne();

        //then
        assertThat(res.getUsername()).isEqualTo("wogud222");
        assertThat(res.getImages().get(0).getImagePath()).isEqualTo("testurl");
    }

    @Test
    @DisplayName("좋아효 갯수 높은것 10개 조회 성공")
    void findPopular10(){
        //when
        List<PostRes> finds = queryFactory.select(Projections.constructor(PostRes.class, post))
                .from(post)
                .orderBy(post.likeCount.desc())
                .limit(10)
                .fetch();

        //then
        assertThat(finds.size()).isEqualTo(10);
        assertThat(finds.get(0).getLikeCount()).isEqualTo(50);
    }

    @Test
    @DisplayName("내 게시물 조회 성공 + 날짜 검색")
    void findMyPosts() {
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
                        post.member.username.eq("wogud222"))
                .orderBy(post.createdAt.desc())
                .fetch();

        int total = queryFactory.select(Projections.constructor(PostRes.class, post))
                .from(post)
                .where(between(startDate, endDate),
                        post.member.username.eq("wogud222"))
                .fetch().size();
        //then
        assertThat(total).isEqualTo(25);
    }

    @Test
    @DisplayName("모든 게시물 조회 성공 + 날짜 검색 + 문자검색 + 정렬 조건")
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
                .orderBy(orderBy(null))
                .fetch();

        int total = queryFactory.select(Projections.constructor(PostRes.class, post))
                .from(post)
                .where(between(startDate, endDate),
                        contains("12"))
                .fetch().size();
        //then
        assertThat(total).isEqualTo(2);
    }

    @Test
    @DisplayName("모든 게시물 조회 성공 + 정렬조건 조회순 ")
    void findPostsOrderByViewCount() {
        //given
        String startDate = "2022-09-01 00:00:00";
        String endDate = "2022-10-30 23:59:59";

        PageRequest pageable = new MyPageReq(1, 10).of();

        //when
        List<PostRes> posts = queryFactory.select(Projections.constructor(PostRes.class, post))
                .from(post)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(between(startDate, endDate))
                .orderBy(orderBy(true))
                .fetch();

        int total = queryFactory.select(Projections.constructor(PostRes.class, post))
                .from(post)
                .where(between(startDate, endDate))
                .fetch().size();
        //then
        assertThat(total).isEqualTo(50);
        assertThat(posts.get(0).getViewCount()).isEqualTo(50);
    }

    @Test
    @DisplayName("모든 게시물 조회 성공 + 날짜 검색 + 문자검색v2")
    void findAllPostsV2() {
        //given
        String startDate = "2022-09-01 00:00:00";
        String endDate = "2022-10-30 23:59:59";

        PageRequest pageable = new MyPageReq(1, 10).of();

        //when
        List<PostRes> posts = queryFactory.select(Projections.constructor(PostRes.class, post,member))
                .from(post)
                .innerJoin(post.member,member)
                .where(between(startDate, endDate),
                        contains("12"))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdAt.desc())
                .fetch();

        int total = queryFactory.select(Projections.constructor(PostRes.class, post,member))
                .from(post)
                .innerJoin(post.member,member)
                .where(between(startDate, endDate),
                        contains("12"))
                .fetch().size();
        //then
        assertThat(total).isEqualTo(2);
    }

    private OrderSpecifier<?> orderBy(Boolean orderType) {
        if(orderType == null) return post.createdAt.desc();
        return post.viewCount.desc();
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