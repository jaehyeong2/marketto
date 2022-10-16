package jjfactory.webclient.business.comment.service;

import jjfactory.webclient.business.comment.domain.Comment;
import jjfactory.webclient.business.comment.dto.req.CommentCreate;
import jjfactory.webclient.business.comment.dto.req.CommentModify;
import jjfactory.webclient.business.comment.repository.CommentRepository;
import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.post.domain.Post;
import jjfactory.webclient.business.post.repository.PostRepository;
import jjfactory.webclient.global.ex.BusinessException;
import jjfactory.webclient.global.ex.ErrorCode;
import jjfactory.webclient.global.util.FireBasePush;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks
    CommentService commentService;
    @Mock
    PostRepository postRepository;
    @Mock
    FireBasePush fireBasePush;
    @Mock
    CommentRepository commentRepository;

    @Test
    @DisplayName("댓글 저장 성공")
    void saveComment() {
        //given
        Member wogud222 = Member.builder()
                .username("wogud222")
                .build();

        Post post = Post.builder()
                .member(wogud222)
                .title("title")
                .content("content")
                .build();

        CommentCreate dto = CommentCreate.builder()
                .content("content")
                .build();

        Comment comment = Comment.builder()
                .member(wogud222)
                .post(post)
                .content("content")
                .build();

        //stub
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));
        when(commentRepository.save(any())).thenReturn(comment);

        //when
        Long commentId = commentService.saveComment(dto, wogud222);

        //then

    }

    @Test
    @DisplayName("작성자 아니면 댓글 삭제 실패")
    void deleteCommentFailed() {
        //given
        Member wogud222 = Member.builder()
                .username("wogud222")
                .build();

        Member wogud333 = Member.builder()
                .username("wogud333")
                .build();

        Comment comment = Comment.builder()
                .member(wogud222)
                .content("content")
                .build();

        //stub
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));

        //expected
        assertThatThrownBy(() -> {
            commentService.deleteComment(comment.getId(),wogud333);
        }).isInstanceOf(BusinessException.class).hasMessage(ErrorCode.HANDLE_ACCESS_DENIED.getMessage());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void modifyComment() {
        //given
        Member wogud222 = Member.builder()
                .username("wogud222")
                .build();

        Comment comment = Comment.builder()
                .member(wogud222)
                .content("content")
                .build();

        CommentModify dto = CommentModify.builder()
                .content("change!")
                .build();
        //stub
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));

        //when
        commentService.modifyComment(comment.getId(),dto,wogud222);

        //then

    }
}