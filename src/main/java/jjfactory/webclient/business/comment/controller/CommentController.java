package jjfactory.webclient.business.comment.controller;

import jjfactory.webclient.business.comment.dto.req.CommentCreate;
import jjfactory.webclient.business.comment.dto.req.CommentModify;
import jjfactory.webclient.business.comment.dto.res.CommentRes;
import jjfactory.webclient.business.comment.dto.res.MyCommentRes;
import jjfactory.webclient.business.comment.service.CommentService;
import jjfactory.webclient.global.config.auth.PrincipalDetails;
import jjfactory.webclient.global.dto.req.MyPageReq;
import jjfactory.webclient.global.dto.res.ApiPageRes;
import jjfactory.webclient.global.dto.res.ApiRes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/comments")
@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ApiPageRes<MyCommentRes> findMyComments(@RequestParam(required = false, defaultValue = "1")int page,
                                                   @RequestParam(required = false, defaultValue = "10")int size,
                                                   @AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ApiPageRes<>(commentService.findMyComments(new MyPageReq(1,10).of(),principalDetails.getMember()));
    }

    @GetMapping("/{postId}")
    public ApiPageRes<CommentRes> findAllComments(@RequestParam(required = false, defaultValue = "1")int page,
                                                  @RequestParam(required = false, defaultValue = "10")int size,
                                                  @PathVariable Long postId){
        return new ApiPageRes<>(commentService.findCommentsByPostId(new MyPageReq(1,10).of(),postId));
    }

    @PostMapping
    public ApiRes<Long> saveComment(@RequestBody CommentCreate dto,
                                    @AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ApiRes<>(commentService.saveComment(dto,principalDetails.getMember()));
    }

    @DeleteMapping("/{commentId}")
    public ApiRes<String> deleteComment(@PathVariable Long commentId,
                                    @AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ApiRes<>(commentService.deleteComment(commentId,principalDetails.getMember()));
    }

    @PatchMapping("/{commentId}")
    public ApiRes<Long> modifyComment(@RequestBody CommentModify dto,
                                    @PathVariable Long commentId,
                                    @AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ApiRes<>(commentService.modifyComment(commentId,dto,principalDetails.getMember()));
    }
}
