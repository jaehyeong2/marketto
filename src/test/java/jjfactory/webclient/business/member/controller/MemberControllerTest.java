package jjfactory.webclient.business.member.controller;

import jjfactory.webclient.business.member.service.AuthService;
import jjfactory.webclient.business.member.service.MemberService;
import jjfactory.webclient.global.config.auth.TokenProvider;
import jjfactory.webclient.global.ex.ExceptionFilter;
import jjfactory.webclient.global.ex.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.context.annotation.ComponentScan.*;

@AutoConfigureRestDocs
@WebMvcTest(controllers = MemberController.class,
        excludeFilters = {
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurerAdapter.class),
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalExceptionHandler.class),
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ExceptionFilter.class)}
)
class MemberControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    MemberService memberService;
    @MockBean
    AuthService authService;

    @Test
    void findMyInfo() {
//        mockMvc.perform()
    }

    @Test
    void withdraw() {
    }

    @Test
    void login() {
    }

    @Test
    void signUp() {
    }
}