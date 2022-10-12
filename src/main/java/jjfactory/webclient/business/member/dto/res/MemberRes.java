package jjfactory.webclient.business.member.dto.res;

import jjfactory.webclient.business.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberRes {
    private Long memberId;
    private String username;
    private String email;
    private String name;

    @Builder
    public MemberRes(Long memberId, String username, String email, String name) {
        this.memberId = memberId;
        this.username = username;
        this.email = email;
        this.name = name;
    }

    public MemberRes(Member member) {
        this.memberId = member.getId();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.name = member.getName();
    }
}
