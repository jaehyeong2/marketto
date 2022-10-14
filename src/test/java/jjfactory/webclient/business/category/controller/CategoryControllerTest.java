package jjfactory.webclient.business.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jjfactory.webclient.TestPrincipalDetailsService;
import jjfactory.webclient.business.category.domain.Category;
import jjfactory.webclient.business.category.dto.req.CategoryCreate;
import jjfactory.webclient.business.category.dto.req.CategoryModify;
import jjfactory.webclient.business.category.repository.CategoryRepository;
import jjfactory.webclient.business.category.service.CategoryService;
import jjfactory.webclient.global.config.auth.PrincipalDetails;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class CategoryControllerIntegratedTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    private final TestPrincipalDetailsService testUserDetailsService = new TestPrincipalDetailsService();
    private PrincipalDetails principalDetails;
    private PrincipalDetails principalDetailsAdmin;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print()).build();

        principalDetails = (PrincipalDetails) testUserDetailsService.loadUserByUsername(TestPrincipalDetailsService.USERNAME);
        principalDetailsAdmin = (PrincipalDetails) testUserDetailsService.loadUserByUsername(TestPrincipalDetailsService.ADMIN);
    }

    @Test
    @DisplayName("카테고리 목록 조회성공")
    void findCategories() throws Exception {
        //given
        for (int i = 1; i < 6; i++) {
            Category category = Category.builder()
                    .name("카테고리"+i)
                    .build();

            categoryRepository.save(category);
        }

        //expected
        mockMvc.perform(get("/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].name").value("카테고리1"))
                .andExpect(jsonPath("$.result[1].name").value("카테고리2"));

        assertThat(categoryRepository.count()).isEqualTo(5);
    }

    @Test
    @DisplayName("일반 유저 접속 시 카테고리 생성 exception")
    void createCategoryFailed() throws Exception {
        //given
        CategoryCreate dto = new CategoryCreate("카테고리1");

        //expected
        mockMvc.perform(post("/admin/category").with(user(principalDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("어드민 접속 시 카테고리 생성 성공")
    void createCategory() throws Exception {
        //given
        CategoryCreate dto = new CategoryCreate("카테고리1");

        //expected
        mockMvc.perform(post("/admin/category").with(user(principalDetailsAdmin))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(1L));

        assertThat(categoryRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("어드민 접속 시 카테고리명 수정 성공")
    void modifyCategory() throws Exception {
        //given
        Category category = Category.builder().name("카테고리1").build();
        categoryRepository.save(category);

        CategoryModify dto = new CategoryModify("바뀜??");

        //expected
        mockMvc.perform(put("/admin/category/"+category.getId()).with(user(principalDetailsAdmin))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(1L));

        //then
        String findName = categoryRepository.findAll().get(0).getName();
        assertThat(findName).isEqualTo("바뀜??");
    }

}