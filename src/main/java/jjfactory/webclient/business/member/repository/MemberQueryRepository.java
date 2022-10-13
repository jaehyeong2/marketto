package jjfactory.webclient.business.member.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.member.domain.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static jjfactory.webclient.business.member.domain.QMember.*;

@RequiredArgsConstructor
@Repository
public class MemberQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Member> findWithdrawMembersBefore3Month(){
        return queryFactory.selectFrom(member)
                .where(member.withdrawDate.before(LocalDateTime.now().minusMonths(3)))
                .fetch();
    }

}
