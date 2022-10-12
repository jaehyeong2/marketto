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
import jjfactory.webclient.global.dto.res.PagingRes;
import jjfactory.webclient.global.ex.BusinessException;
import jjfactory.webclient.global.ex.ErrorCode;
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

    @Transactional(readOnly = true)
    public PagingRes<MyCommentRes> findMyComments(Pageable pageable, Member member){
        return new PagingRes<>(commentQueryRepository.findMyComments(pageable,member));
    }

    @Transactional(readOnly = true)
    public PagingRes<CommentRes> findCommentsByPostId(Pageable pageable, Long postId){
        return new PagingRes<>(commentQueryRepository.findComments(pageable,postId));
    }

    public Long saveComment(CommentCreate dto,Member member){
        Post post = getPost(dto);

        Comment comment =  Comment.create(dto, member, post);
        if(dto.getParentId() != null){
            Comment parent = getComment(dto.getParentId());
            comment.addParentComment(parent);
        }

        commentRepository.save(comment);

        return comment.getId();
    }

    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }

    public void modifyComment(Long commentId, CommentModify dto){
        Comment comment = getComment(commentId);

        comment.modify(dto.getContent());
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
