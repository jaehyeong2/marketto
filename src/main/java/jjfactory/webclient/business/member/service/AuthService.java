package jjfactory.webclient.business.member.service;


import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.member.dto.req.LoginReq;
import jjfactory.webclient.business.member.dto.req.MemberCreate;
import jjfactory.webclient.business.member.dto.res.TokenRes;
import jjfactory.webclient.business.member.repository.MemberRepository;
import jjfactory.webclient.global.config.auth.TokenProvider;
import jjfactory.webclient.global.ex.BusinessException;
import jjfactory.webclient.global.ex.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public Long join(MemberCreate req) {
        hyphenCheck(req.getPhone());
        String encPassword = passwordEncoder.encode(req.getPassword());
        Member member = Member.create(req, encPassword);
        memberRepository.save(member);

        return member.getId();
    }

    @Transactional(readOnly = true)
    public TokenRes login(LoginReq dto){
        Member member = memberRepository.findMemberByUsername(dto.getUsername()).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NOT_FOUND_USER);
        });

        matchPassword(dto.getPassword(),member.getPassword());
        String token = createToken(member);

        return new TokenRes(token);
    }

    //jwt 토큰생성
    public String createToken(Member member){
        return tokenProvider.createToken(member.getUsername(),member.getRole());
    }

    public void matchPassword(String reqPassword, String userPassword){
        boolean matches = passwordEncoder.matches(reqPassword, userPassword);

        if(!matches){
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }

    private void hyphenCheck(String number) {
        if (number.contains("-") || (!number.matches("[+-]?\\d*(\\.\\d+)?"))){
            throw new BusinessException(ErrorCode.INVALID_TYPE_VALUE2);
        }
    }

    private Member getMember(Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(()->{
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
        });
        return findMember;
    }
}
