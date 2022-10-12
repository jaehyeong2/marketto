package jjfactory.webclient.business.member.service;


import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.member.dto.req.MemberCreate;
import jjfactory.webclient.business.member.dto.req.MemberUpdate;
import jjfactory.webclient.business.member.dto.res.MemberRes;
import jjfactory.webclient.business.member.repository.MemberRepository;
import jjfactory.webclient.global.ex.BusinessException;
import jjfactory.webclient.global.ex.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;

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

    public void withdraw(Long memberId) {
        Member member = getMember(memberId);
        member.withdraw();
    }

    private Member getMember(Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(()->{
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
        });
        return findMember;
    }
}
