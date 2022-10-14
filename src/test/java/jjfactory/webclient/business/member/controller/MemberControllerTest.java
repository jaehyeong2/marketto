package jjfactory.webclient.business.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jjfactory.webclient.TestPrincipalDetailsService;
import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.member.dto.req.LoginReq;
import jjfactory.webclient.business.member.dto.req.MemberCreate;
import jjfactory.webclient.business.member.dto.res.MemberRes;
import jjfactory.webclient.business.member.dto.res.TokenRes;
import jjfactory.webclient.business.member.service.AuthService;
import jjfactory.webclient.business.member.service.MemberService;
import jjfactory.webclient.global.config.auth.PrincipalDetails;
import jjfactory.webclient.global.config.auth.TokenProvider;
import jjfactory.webclient.global.ex.ExceptionFilter;
import jjfactory.webclient.global.ex.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.context.annotation.ComponentScan.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(controllers = MemberController.class,
        excludeFilters = {
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurerAdapter.class),
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalExceptionHandler.class),
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ExceptionFilter.class)}
)
class MemberControllerTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    MemberService memberService;
    @MockBean
    AuthService authService;
    @Autowired
    ObjectMapper mapper;

    private final TestPrincipalDetailsService testUserDetailsService = new TestPrincipalDetailsService();
    private PrincipalDetails principalDetails;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print()).build();

        principalDetails = (PrincipalDetails) testUserDetailsService.loadUserByUsername(TestPrincipalDetailsService.USERNAME);
    }

    @Test
    @DisplayName("내 정보 조회 성공")
    void findMyInfo() throws Exception {
        //given
        MemberRes res = new MemberRes(principalDetails.getMember());

        //stub
        given(memberService.getMyInfo(principalDetails.getMember())).willReturn(res);

        //expected
        mockMvc.perform(get("/members").with(user(principalDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.username").value("wogud222"));
    }

    @Test
    @DisplayName("회원탈퇴 성공")
    void withdraw() throws Exception {
        //given
        Member member = principalDetails.getMember();

        //stub
        given(memberService.withdraw(member)).willReturn("ok");

        //expected
        mockMvc.perform(delete("/members/withdraw").with(user(principalDetails)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("ok"));

    }

    @DisplayName("회원가입 성공")
    @WithMockUser
    @Test
    void signUp() throws Exception {
        //given
        MemberCreate dto = MemberCreate.builder()
                .name("이재형")
                .username("test1234")
                .email("wogud22@22.com")
                .password("1234")
                .phone("01012341234")
                .build();

        //stub
        given(authService.join(any())).willReturn(1L);

        //expected
        mockMvc.perform(post("/members/signup").with(csrf()).content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(1L));
    }

    @DisplayName("로그인 성공")
    @WithMockUser
    @Test
    void login() throws Exception {
        //given
        loginSetUp();

        LoginReq loginReq = LoginReq.builder()
                .username("test1234")
                .password("1234").build();

        //stub
        given(authService.login(any())).willReturn(new TokenRes("testToken!!"));

        //expected
        mockMvc.perform(post("/members/login").with(csrf()).content(mapper.writeValueAsString(loginReq))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.jwtToken").value("testToken!!"));
    }

    private void loginSetUp() {
        MemberCreate dto = MemberCreate.builder()
                .name("이재형")
                .username("test1234")
                .email("wogud22@22.com")
                .password("1234")
                .phone("01012341234")
                .build();

        authService.join(dto);
    }
}