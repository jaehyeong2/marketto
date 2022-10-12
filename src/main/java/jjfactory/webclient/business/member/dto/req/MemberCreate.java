package jjfactory.webclient.business.member.dto.req;

import jjfactory.webclient.business.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MemberCreate {
    @NotBlank
    private String name;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String phone;

    @Builder
    public MemberCreate(String name, String password, String email, String username, String phone) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.username = username;
        this.phone = phone;
    }
}
