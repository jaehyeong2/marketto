package jjfactory.webclient.business.member.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class MemberUpdate {
    @NotBlank
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String phone;

    @Builder
    public MemberUpdate(String email, String username, String phone) {
        this.email = email;
        this.username = username;
        this.phone = phone;
    }
}
