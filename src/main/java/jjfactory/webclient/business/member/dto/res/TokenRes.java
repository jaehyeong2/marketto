package jjfactory.webclient.business.member.dto.res;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TokenRes {
    private String jwtToken;
}
