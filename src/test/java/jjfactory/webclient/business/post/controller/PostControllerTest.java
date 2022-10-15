package jjfactory.webclient.business.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jjfactory.webclient.TestPrincipalDetailsService;
import jjfactory.webclient.business.comment.controller.CommentController;
import jjfactory.webclient.business.comment.dto.req.CommentModify;
import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.post.domain.PostImage;
import jjfactory.webclient.business.post.dto.req.PostCreate;
import jjfactory.webclient.business.post.dto.req.PostUpdate;
import jjfactory.webclient.business.post.dto.res.PostDetailRes;
import jjfactory.webclient.business.post.dto.res.PostImageRes;
import jjfactory.webclient.business.post.dto.res.PostRes;
import jjfactory.webclient.business.post.service.PostService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(controllers = PostController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurerAdapter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalExceptionHandler.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ExceptionFilter.class)}
)
class PostControllerTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    PostService postService;
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

    @WithMockUser
    @Test
    @DisplayName("모든 게시물 조회 성공")
    void findAllPosts() throws Exception {
        //given
        PageRequest pageable = new MyPageReq(1, 10).of();
        Boolean orderType = true;

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("startDate","2022-01-01 00:00:00");
        map.add("endDate","2022-12-31 23:59:59");
        map.add("page", String.valueOf(1));
        map.add("size",String.valueOf(10));
        map.add("query","글1");
        map.add("orderType",orderType.toString());

        PostRes res1 = PostRes.builder()
                .username("wogud111")
                .viewCount(0)
                .title("하이~")
                .build();

        PostRes res2 = PostRes.builder()
                .username("wogud222")
                .viewCount(0)
                .title("하이~")
                .build();

        List<PostRes> list = List.of(res1, res2);
        PageImpl<PostRes> page = new PageImpl<>(list, pageable, list.size());
        PagingRes<PostRes> pagingRes = new PagingRes<>(page);

        //stub
        given(postService.findAllPosts(any(Pageable.class),anyString(),anyString(),anyString(),anyBoolean()))
                .willReturn(pagingRes);

        //expected
        mockMvc.perform(get("/post/all").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.totalCount").value(2))
                .andExpect(jsonPath("$.result.resultList[0].username").value("wogud111"))
                .andExpect(jsonPath("$.result.resultList[1].username").value("wogud222"));

    }

    @Test
    @DisplayName("내 게시물 조회 성공")
    void findMyPosts() throws Exception {
        //given
        PageRequest pageable = new MyPageReq(1, 10).of();

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("startDate","2022-01-01 00:00:00");
        map.add("endDate","2022-12-31 23:59:59");
        map.add("page", String.valueOf(1));
        map.add("size",String.valueOf(10));

        PostRes res1 = PostRes.builder()
                .username("wogud111")
                .viewCount(0)
                .title("하이~")
                .build();

        PostRes res2 = PostRes.builder()
                .username("wogud222")
                .viewCount(0)
                .title("하이~")
                .build();

        List<PostRes> list = List.of(res1, res2);
        PageImpl<PostRes> page = new PageImpl<>(list, pageable, list.size());
        PagingRes<PostRes> pagingRes = new PagingRes<>(page);

        //stub
        given(postService.findMyPosts(any(Pageable.class),anyString(),anyString(),any(Member.class)))
                .willReturn(pagingRes);

        //expected
        mockMvc.perform(get("/post").with(user(principalDetails)).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.totalCount").value(2))
                .andExpect(jsonPath("$.result.resultList[0].username").value("wogud111"))
                .andExpect(jsonPath("$.result.resultList[1].username").value("wogud222"));
    }

    @Test
    @DisplayName("게시물 단건 조회")
    void findPost() throws Exception {
        //given
        PostDetailRes res = PostDetailRes.builder()
                .username("wogud111")
                .viewCount(0)
                .title("하이~")
                .build();

        //stub
        given(postService.findPost(anyLong())).willReturn(res);

        //expected
        mockMvc.perform(get("/post/1").with(user(principalDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.username").value("wogud111"))
                .andExpect(jsonPath("$.result.viewCount").value(0))
                .andExpect(jsonPath("$.result.title").value("하이~"));
    }

    @Test
    @DisplayName("게시물 생성")
    void createPost() throws Exception{
        //given
        PostCreate create = PostCreate.builder()
                .categoryId(1L)
                .price(1000)
                .title("ㅎㅇ")
                .content("나 재형스")
                .build();

        String stringJson = mapper.writeValueAsString(create);
        MockMultipartFile dto = new MockMultipartFile("dto", "dto",
                MediaType.APPLICATION_JSON_VALUE,
                stringJson.getBytes(StandardCharsets.UTF_8));

        MockMultipartFile image = new MockMultipartFile("images", "test.txt",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "test file".getBytes(StandardCharsets.UTF_8) );

        //stub
        given(postService.savePost(any(),any(),any())).willReturn(1L);

        //expected
        mockMvc.perform(multipart("/post")
                        .file(dto).file(image)
                        .with(user(principalDetails)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(1L));
    }

    @Test
    @DisplayName("게시물 삭제 성공")
    void deleteBoard() throws Exception {
        //stub
        given(postService.deleteById(anyLong(),any(Member.class))).willReturn("ok");

        //expected
        mockMvc.perform(delete("/post/1").with(user(principalDetails)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("ok"));
    }

    @Test
    @DisplayName("게시물 수정 성공")
    void updatePost() throws Exception{
        //given
        PostUpdate dto = PostUpdate.builder()
                .content("수정!")
                .build();

        //stub
        given(postService.update(any(PostUpdate.class),anyLong(),any(Member.class))).willReturn(1L);

        //expected
        mockMvc.perform(patch("/post/1").with(user(principalDetails)).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(1L));
    }
}