package jjfactory.webclient.business.member.service;


import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.member.dto.req.MemberCreate;
import jjfactory.webclient.business.member.dto.req.MemberUpdate;
import jjfactory.webclient.business.member.dto.res.MemberRes;
import jjfactory.webclient.business.member.repository.MemberQueryRepository;
import jjfactory.webclient.business.member.repository.MemberRepository;
import jjfactory.webclient.global.ex.BusinessException;
import jjfactory.webclient.global.ex.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;

    @Transactional(readOnly = true)
    public MemberRes getMyInfo(Member member) {
        Member findMember = getMember(member.getId());
        return new MemberRes(findMember);
    }

//    @Transactional(readOnly = true)
//    public Member findByKakaoId(Long id) {
//
//        return memberRepository.findByKakaoId(id)
//                .orElseThrow(NotFoundMemberException::new);
//    }

//    @Transactional(readOnly = true)
//    public boolean existsByKakaoId(Long id) {
//        Optional<Member> findMember = memberRepository.findByKakaoId(id);
//
//        return findMember.isPresent();
//    }

    public void update(Member member, MemberUpdate req) {
        Member findMember = getMember(member.getId());
        findMember.modify(req);
    }

    public String withdraw(Member member) {
        member.withdraw(LocalDateTime.now());
        return "ok";
    }

    @Scheduled(cron = "0 5 0 * * *") //초 분 시 일 월 요일
    public void deleteMembers(){
        memberQueryRepository.findWithdrawMembersBefore3Month();
    }

    private Member getMember(Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(()->{
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
        });
        return findMember;
    }
}
