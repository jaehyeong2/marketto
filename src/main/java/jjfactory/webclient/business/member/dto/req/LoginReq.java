package jjfactory.webclient.business.member.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class LoginReq {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @Builder
    public LoginReq(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
