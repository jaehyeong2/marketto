package jjfactory.webclient;

import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.member.domain.Role;
import jjfactory.webclient.global.config.auth.PrincipalDetails;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

@Profile("test")
public class TestPrincipalDetailsService implements UserDetailsService {

    public static final String USERNAME = "wogud222";
    public static final String ADMIN = "admin";

    private Member getMember() {
        return Member.builder()
                .username(USERNAME)
                .name("이재형")
                .password("1234")
                .email("wogud1514@naver.com")
                .phone("01012341234")
                .role(Role.USER)
                .build();
    }

    private Member getAdmin() {
        return Member.builder()
                .username(ADMIN)
                .name("이재형")
                .password("1234")
                .email("wogud1514@naver.com")
                .phone("01012341234")
                .role(Role.ADMIN)
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        if (name.equals(USERNAME)) {
            return new PrincipalDetails(getMember());
        }else if(name.equals(ADMIN)){
            return new PrincipalDetails(getAdmin());
        }
        return null;
    }
}
