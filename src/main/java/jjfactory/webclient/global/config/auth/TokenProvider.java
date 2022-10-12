package jjfactory.webclient.global.config.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jjfactory.webclient.business.member.domain.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;


// jwt 토큰 생성
@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

    @Value("spring.jwt.secret")
    private String secretKey;


    private static final long TOKEN_TIME = 1000L * 60 * 30;
    private static final long REFRESH_TOKEN_VALID_TIME =   1000L * 60 * 24 * 700;
    private final PrincipalDetailsService principalDetailsService;

    @PostConstruct
    protected  void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String userId, Role role){
        // Claims 란 jwt 바디 부분
        // 바디부분에 유저에 대한 정보를 넣는다
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("roles",role);
        Date now =new Date();


        return Jwts.builder().setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }


    public Authentication getAuthentication(String token){
        UserDetails user= principalDetailsService.loadUserByUsername(getUserId(token));

        if(user == null){
            throw new EntityNotFoundException();
        }

        return new UsernamePasswordAuthenticationToken(user,"",user.getAuthorities());
    }



    //토큰을 복호화 하고 body에 저장된 유저 id 를 꺼낸다
    public String getUserId(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }


    public String resolveToken(HttpServletRequest req){
        return req.getHeader(HttpHeaders.AUTHORIZATION);
    }


    public boolean validateToken(String token){
        Jws<Claims> claimsJwt = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return !claimsJwt.getBody().getExpiration().before(new Date());
    }

    public String createJwtRefreshToken(String userId,List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("roles",roles);
        Date now = new Date();
        Date expiration = new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
