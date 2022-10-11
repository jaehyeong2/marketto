package jjfactory.webclient.business.member.repository;

import jjfactory.webclient.business.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
