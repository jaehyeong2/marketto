package jjfactory.webclient.business.post.service;

import jjfactory.webclient.business.category.domain.Category;
import jjfactory.webclient.business.category.repository.CategoryRepository;
import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.post.domain.Post;
import jjfactory.webclient.business.post.domain.PostImage;
import jjfactory.webclient.business.post.domain.PostLike;
import jjfactory.webclient.business.post.domain.report.Report;
import jjfactory.webclient.business.post.domain.report.ReportReason;
import jjfactory.webclient.business.post.dto.req.PostCreate;
import jjfactory.webclient.business.post.dto.req.PostImageCreate;
import jjfactory.webclient.business.post.dto.req.PostUpdate;
import jjfactory.webclient.business.post.dto.req.ReportCreate;
import jjfactory.webclient.business.post.dto.res.PostDetailRes;
import jjfactory.webclient.business.post.dto.res.PostRes;
import jjfactory.webclient.business.post.repository.*;
import jjfactory.webclient.global.ex.BusinessException;
import jjfactory.webclient.global.ex.ErrorCode;
import jjfactory.webclient.global.util.FireBasePush;
import jjfactory.webclient.global.util.s3.S3UploaderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @InjectMocks
    PostService postService;
    @Mock
    PostRepository postRepository;
    @Spy
    PostLikeRepository postLikeRepository;
    @Mock
    FireBasePush fireBasePush;
    @Mock
    PostQueryRepository postQueryRepository;
    @Mock
    ReportRepository reportRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    S3UploaderService s3UploaderService;
    @Mock
    PostImageRepository postImageRepository;

    @Test
    @DisplayName("????????? ?????? ?????? ??????")
    void findPost() {
        //given
        Post post = Post.builder()
                .content("content")
                .title("title")
                .build();

        PostDetailRes res = PostDetailRes.builder()
                .content("content")
                .title("title")
                .username("wogud2")
                .build();

        //stub
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));
        when(postQueryRepository.findPost(any())).thenReturn(res);

        //when
        PostDetailRes result = postService.findPost(post.getId());

        //then
        assertThat(result.getContent()).isEqualTo("content");
        assertThat(result.getTitle()).isEqualTo("title");
        assertThat(result.getUsername()).isEqualTo("wogud2");
    }

    @Test
    @DisplayName("????????? ?????? ??????")
    void savePost() {
        //given
        Member wogud222 = Member.builder()
                .username("wogud222")
                .build();

        Category category = Category.builder().name("cate").build();

        PostCreate dto = PostCreate.builder()
                .title("title").content("content").categoryId(1L)
                .build();

        MultipartFile image = new MockMultipartFile("name","content".getBytes());

        //stub
        when(categoryRepository.findById(any())).thenReturn(Optional.ofNullable(category));
        when(postImageRepository.saveAll(any())).thenReturn(Collections.singletonList(PostImage.builder().build()));
        when(s3UploaderService.upload(any(),anyString())).thenReturn(PostImageCreate
                .builder()
                .build());

        //when
        postService.savePost(dto, Collections.singletonList(image),wogud222);
    }

    @Test
    @DisplayName("?????? ????????? ????????? ?????? ??? exception")
    void likeFailed() {
        //given
        Member wogud222 = Member.builder()
                .username("wogud222")
                .build();

        Post post = Post.builder()
                .content("content")
                .title("title")
                .member(wogud222)
                .build();

        //stub
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));

        //expected
        assertThatThrownBy(() -> {
            postService.like(wogud222, post.getId());
        }).isInstanceOf(BusinessException.class).hasMessage(ErrorCode.HANDLE_ACCESS_DENIED.getMessage());

    }

    @Test
    @DisplayName("????????? ??????")
    void like() {
        //given
        Member wogud222 = Member.builder()
                .username("wogud222")
                .build();

        Member wogud333 = Member.builder()
                .username("wogud333")
                .build();

        Post post = Post.builder()
                .content("content")
                .title("title")
                .member(wogud222)
                .build();

        PostLike like = PostLike.builder()
                .member(wogud333).post(post)
                .build();

        //stub
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));
        when(postLikeRepository.save(any())).thenReturn(like);

        fireBasePush = mock(FireBasePush.class);
        doNothing().when(fireBasePush).sendMessage(any());

        //when
        Long likeId = postService.like(wogud333, post.getId());

        //then
        assertThat(likeId).isNotNull();
    }

    @Test
    @DisplayName("????????? ?????? ??????")
    void report() {
        //given
        Member wogud222 = Member.builder()
                .username("wogud222")
                .build();

        Member wogud333 = Member.builder()
                .username("wogud333")
                .build();

        Post post = Post.builder()
                .content("content")
                .title("title")
                .member(wogud333)
                .build();

        ReportCreate dto = ReportCreate.builder()
                .postId(post.getId())
                .reason(ReportReason.INVALID_CONTENT)
                .build();

        Report report = Report.builder()
                .post(post)
                .reason(ReportReason.INVALID_CONTENT)
                .build();

        //stub
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));
        when(reportRepository.save(any())).thenReturn(report);

        //when
        postService.report(wogud222, dto);

        //then
        //TODO getId????????? ????????? ????????????????????? ????????? ??????????????? ????????????
    }

    @Test
    @DisplayName("????????? ????????? ????????? ?????? ??????")
    void updateFail() {
        //given
        Member wogud222 = Member.builder()
                .username("wogud222")
                .build();

        Member wogud333 = Member.builder()
                .username("wogud333")
                .build();

        Post post = Post.builder()
                .content("content")
                .title("title")
                .member(wogud222)
                .build();

        PostUpdate updateDto = PostUpdate.builder().content("???????").title("?????????")
                .build();

        //stub
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));

        //expected
        assertThatThrownBy(() -> {
            postService.update(updateDto,post.getId(),wogud333);;
        }).isInstanceOf(BusinessException.class).hasMessage(ErrorCode.HANDLE_ACCESS_DENIED.getMessage());
    }

    @Test
    @DisplayName("????????? ?????? ??????")
    void update() {
        //given
        Member wogud222 = Member.builder()
                .username("wogud222")
                .build();

        Member wogud333 = Member.builder()
                .username("wogud333")
                .build();

        Post post = Post.builder()
                .content("content")
                .title("title")
                .member(wogud222)
                .build();

        PostUpdate updateDto = PostUpdate.builder().content("???????").title("?????????")
                .build();

        //stub
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));

        //when
        postService.update(updateDto,post.getId(),wogud222);

        //then
    }

    @Test
    @DisplayName("????????? ????????? ????????? ?????? ??????")
    void deleteByIdFailed() {
        //given
        Member wogud222 = Member.builder()
                .username("wogud222")
                .build();

        Member wogud333 = Member.builder()
                .username("wogud333")
                .build();

        Post post = Post.builder()
                .content("content")
                .title("title")
                .member(wogud222)
                .build();

        //stub
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));

        //expected
        assertThatThrownBy(() -> {
            postService.deleteById(post.getId(),wogud333);;
        }).isInstanceOf(BusinessException.class).hasMessage(ErrorCode.HANDLE_ACCESS_DENIED.getMessage());
    }
}