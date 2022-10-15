package jjfactory.webclient.business.post.controller;


import io.swagger.annotations.ApiOperation;
import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.post.dto.req.PostCreate;
import jjfactory.webclient.business.post.dto.req.PostUpdate;
import jjfactory.webclient.business.post.dto.res.PostDetailRes;
import jjfactory.webclient.business.post.dto.res.PostRes;
import jjfactory.webclient.business.post.service.PostService;
import jjfactory.webclient.global.config.auth.PrincipalDetails;
import jjfactory.webclient.global.dto.req.MyPageReq;
import jjfactory.webclient.global.dto.res.ApiPageRes;
import jjfactory.webclient.global.dto.res.ApiRes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/post")
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    @GetMapping("/all")
    @ApiOperation(value = "모든 게시글 조화" , notes = "모든 게시글 페이징, 날짜 검색 및 키워드 검색가능")
    public ApiPageRes<PostRes> findAllPosts(@RequestParam(required = false, defaultValue = "1") int page,
                                            @RequestParam(required = false, defaultValue = "10") int size,
                                            @RequestParam(required = false) String startDate,
                                            @RequestParam(required = false) String endDate,
                                            @RequestParam(required = false) String query,
                                            @RequestParam(required = false) Boolean orderType){
        return new ApiPageRes<>(postService.findAllPosts(new MyPageReq(page,size).of(),startDate,endDate,query,orderType));
    }
    @GetMapping
    @ApiOperation(value = "내 게시글 조화" , notes = "모든 게시글 페이징, 날짜 검색 및 키워드 검색가능")
    public ApiPageRes<PostRes> findMyPosts(@RequestParam(required = false, defaultValue = "1") int page,
                                           @RequestParam(required = false, defaultValue = "10") int size,
                                           @RequestParam(required = false) String startDate,
                                           @RequestParam(required = false) String endDate,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ApiPageRes<>(postService.findMyPosts(new MyPageReq(page,size).of(),startDate,endDate,principalDetails.getMember()));
    }

    @GetMapping("/{postId}")
    @ApiOperation(value = "게시글 단건 조회" )
    public ApiRes<PostDetailRes> findPost(@PathVariable Long postId){
        return new ApiRes<>(postService.findPost(postId));
    }

    @PostMapping
    @ApiOperation(value = "게시글 생성" , notes = "@RequestPart이용. ")
    public ApiRes<Long> createPost(@Valid @RequestPart PostCreate dto,
                                   @RequestPart List<MultipartFile> images,
                                   @AuthenticationPrincipal Member member){
        return new ApiRes<>(postService.savePost(dto,images,member));
    }

    @DeleteMapping("/{postId}")
    @ApiOperation(value = "게시글 삭제")
    public ApiRes<String> deleteBoard(@PathVariable Long postId,
                                     @AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ApiRes<>(postService.deleteById(postId,principalDetails.getMember()));
    }

    @PatchMapping("/{postId}")
    @ApiOperation(value = "게시글 수정")
    public ApiRes<Long> updatePost(@PathVariable Long postId,
                                   @Valid  @RequestBody PostUpdate dto,
                                   @AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ApiRes<>(postService.update(dto,postId,principalDetails.getMember()));
    }
}
