package jjfactory.webclient.business.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jjfactory.webclient.TestPrincipalDetailsService;
import jjfactory.webclient.business.comment.domain.Comment;
import jjfactory.webclient.business.comment.dto.req.CommentCreate;
import jjfactory.webclient.business.comment.dto.req.CommentModify;
import jjfactory.webclient.business.comment.dto.res.CommentRes;
import jjfactory.webclient.business.comment.dto.res.MyCommentRes;
import jjfactory.webclient.business.comment.service.CommentService;
import jjfactory.webclient.business.member.controller.MemberController;
import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.member.dto.res.MemberRes;
import jjfactory.webclient.business.post.domain.Post;
import jjfactory.webclient.global.config.auth.PrincipalDetails;
import jjfactory.webclient.global.dto.req.MyPageReq;
import jjfactory.webclient.global.dto.res.PagingRes;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(controllers = CommentController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurerAdapter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalExceptionHandler.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ExceptionFilter.class)}
)
class CommentControllerTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    CommentService commentService;

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
    @DisplayName("??? ?????? ??????")
    void findMyComments() throws Exception {
        //given
        PageRequest pageable = new MyPageReq(1, 10).of();

        MyCommentRes res = MyCommentRes.builder()
                .content("??????1")
                .postTitle("??? ??????")
                .build();

        MyCommentRes res2 = MyCommentRes.builder()
                .content("??????2")
                .postTitle("??? ??????")
                .build();

        List<MyCommentRes> list = List.of(res, res2);
        PageImpl<MyCommentRes> page = new PageImpl<>(list, pageable, list.size());
        PagingRes<MyCommentRes> pagingRes = new PagingRes<>(page);

        //stub
        given(commentService.findMyComments(any(Pageable.class),any(Member.class))).willReturn(pagingRes);


        //expected
        mockMvc.perform(get("/comments").with(user(principalDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.totalCount").value(2))
                .andExpect(jsonPath("$.result.resultList[0].content").value("??????1"));
    }

    @Test
    @DisplayName("??? ?????? ?????? ??????")
    void findAllComments() throws Exception {
        //given
        PageRequest pageable = new MyPageReq(1, 10).of();

        CommentRes res = CommentRes.builder()
                .content("??????")
                .build();

        CommentRes res2 = CommentRes.builder()
                .content("??????")
                .child(Collections.singletonList(res))
                .build();

        List<CommentRes> list = List.of(res, res2);
        PageImpl<CommentRes> page = new PageImpl<>(list, pageable, list.size());
        PagingRes<CommentRes> pagingRes = new PagingRes<>(page);

        //stub
        given(commentService.findCommentsByPostId(any(Pageable.class),anyLong())).willReturn(pagingRes);

        //expected
        mockMvc.perform(get("/comments/1").with(user(principalDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.totalCount").value(2))
                .andExpect(jsonPath("$.result.resultList[1].child[0].content").value("??????"));
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void saveComment() throws Exception {
        //given
        CommentCreate dto = CommentCreate.builder().content("??????").boardId(8L).build();

        //stub
        given(commentService.saveComment(any(CommentCreate.class),any(Member.class))).willReturn(1L);

        //expected
        mockMvc.perform(post("/comments").with(user(principalDetails)).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(1L));
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void deleteComment() throws Exception {
        //stub
        given(commentService.deleteComment(anyLong(),any(Member.class))).willReturn("ok");

        //expected
        mockMvc.perform(delete("/comments/1").with(user(principalDetails)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("ok"));
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void modifyComment() throws Exception {
        //given
        CommentModify dto = CommentModify.builder().content("?????????!!").build();

        //stub
        given(commentService.modifyComment(anyLong(),any(CommentModify.class),any(Member.class))).willReturn(1L);

        //expected
        mockMvc.perform(patch("/comments/1").with(user(principalDetails)).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(1L));
    }
}