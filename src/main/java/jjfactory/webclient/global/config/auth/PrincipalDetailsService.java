package jjfactory.webclient.global.config.auth;

import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.member.repository.MemberRepository;
import jjfactory.webclient.global.ex.BusinessException;
import jjfactory.webclient.global.ex.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberByUsername(username).orElseThrow(()->{
            throw new BusinessException(ErrorCode.NOT_FOUND_USER);
        });

        return new PrincipalDetails(member);
    }
}
