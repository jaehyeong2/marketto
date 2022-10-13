package jjfactory.webclient.business.member.domain;

import jjfactory.webclient.business.BaseEntity;
import jjfactory.webclient.business.member.dto.req.MemberCreate;
import jjfactory.webclient.business.member.dto.req.MemberUpdate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String username;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;
    private LocalDateTime withdrawDate;

    @Builder
    public Member(String name, String email, String phone, String password, Role role, String username, MemberStatus status) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.username = username;
        this.status = status;
    }

    public static Member create(MemberCreate dto, String encPassword){
        return Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(encPassword)
                .username(dto.getUsername())
                .role(Role.USER)
                .phone(dto.getPhone())
                .status(MemberStatus.ACT)
                .build();
    }

    public void modify(MemberUpdate dto) {
        this.email = dto.getEmail();
        this.username = dto.getUsername();
        this.phone = dto.getPhone();
    }

    public void withdraw(LocalDateTime withdrawDate) {
        this.status = MemberStatus.NON;
        this.withdrawDate = withdrawDate;
    }
}
