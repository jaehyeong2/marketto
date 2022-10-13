package jjfactory.webclient.business.comment.service;

import jjfactory.webclient.business.comment.domain.Comment;
import jjfactory.webclient.business.comment.dto.req.CommentCreate;
import jjfactory.webclient.business.comment.dto.req.CommentModify;
import jjfactory.webclient.business.comment.dto.res.CommentRes;
import jjfactory.webclient.business.comment.dto.res.MyCommentRes;
import jjfactory.webclient.business.comment.repository.CommentQueryRepository;
import jjfactory.webclient.business.comment.repository.CommentRepository;
import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.post.domain.Post;
import jjfactory.webclient.business.post.repository.PostRepository;
import jjfactory.webclient.global.dto.req.FcmMessageDto;
import jjfactory.webclient.global.dto.res.PagingRes;
import jjfactory.webclient.global.ex.BusinessException;
import jjfactory.webclient.global.ex.ErrorCode;
import jjfactory.webclient.global.util.FireBasePush;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentQueryRepository commentQueryRepository;
    private final FireBasePush fireBasePush;

    @Transactional(readOnly = true)
    public PagingRes<MyCommentRes> findMyComments(Pageable pageable, Member member){
        return new PagingRes<>(commentQueryRepository.findMyComments(pageable,member));
    }

    @Transactional(readOnly = true)
    public PagingRes<CommentRes> findCommentsByPostId(Pageable pageable, Long postId){
        return new PagingRes<>(commentQueryRepository.findComments(pageable,postId));
    }

    public Long saveComment(CommentCreate dto,Member loginMember){
        Post post = getPost(dto);

        Comment comment =  Comment.create(dto, loginMember, post);
        if(dto.getParentId() != null && !dto.getParentId().equals(0L)){
            Comment parent = getComment(dto.getParentId());
            comment.addParentComment(parent);

            fireBasePush.sendMessage(FcmMessageDto.builder()
                    .fcmToken(parent.getMember().getFcmToken())
                    .title(loginMember.getUsername()+"님이 회원님의 댓글에 답글을 남겼습니다.")
                    .build());
        }

        commentRepository.save(comment);
        fireBasePush.sendMessage(FcmMessageDto.builder()
                .fcmToken(post.getMember().getFcmToken())
                .title(loginMember.getUsername()+"님이 회원님의 게시물에 댓글을 남겼습니다.")
                .build());

        return comment.getId();
    }

    public String deleteComment(Long commentId,Member loginMember){
        Comment comment = getComment(commentId);

        validateMember(loginMember, comment);
        commentRepository.deleteById(commentId);
        return "ok";
    }

    public Long modifyComment(Long commentId, CommentModify dto, Member loginMember){
        Comment comment = getComment(commentId);

        validateMember(loginMember, comment);
        comment.modify(dto.getContent());

        return comment.getId();
    }

    private void validateMember(Member loginMember, Comment comment) {
        if(!comment.getMember().equals(loginMember)){
            throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
        }
    }

    private Comment getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
        });
        return comment;
    }

    private Post getPost(CommentCreate dto) {
        Post post = postRepository.findById(dto.getBoardId()).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
        });
        return post;
    }
}
