package jjfactory.webclient.business.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.member.domain.MemberStatus;
import jjfactory.webclient.global.config.QueryDslConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static jjfactory.webclient.business.member.domain.QMember.member;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Import(QueryDslConfig.class)
@DataJpaTest
class MemberQueryRepositoryTest {

    @Autowired
    EntityManager em;
    private JPAQueryFactory queryFactory;

    @BeforeEach
    void init(){
        queryFactory = new JPAQueryFactory(em);
    }


    @Test
    @DisplayName("3개월전에 탈퇴한 회원들 조회 성공")
    void findWithdrawMembersBefore3Month() {
        //given
        Member test1 = Member.builder()
                .username("test1")
                .build();

        Member test2 = Member.builder()
                .username("test2")
                .build();

        Member test3 = Member.builder()
                .username("test3")
                .build();

        em.persist(test1);
        em.persist(test2);
        em.persist(test3);

        test1.withdraw(LocalDateTime.of(2022,6,1,0,0,0));
        test2.withdraw(LocalDateTime.now());
        test2.withdraw(LocalDateTime.of(2022,10,1,0,0,0));

        //when
        List<Member> members = queryFactory.selectFrom(member)
                .where(member.withdrawDate.before(LocalDateTime.now().minusMonths(3)))
                .fetch();

        //then
        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0).getUsername()).isEqualTo("test1");
        assertThat(members.get(0).getStatus()).isEqualTo(MemberStatus.NON);

    }
}