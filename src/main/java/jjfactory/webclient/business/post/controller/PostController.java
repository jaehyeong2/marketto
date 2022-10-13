package jjfactory.webclient.business.post.controller;


import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.post.dto.req.PostCreate;
import jjfactory.webclient.business.post.service.PostService;
import jjfactory.webclient.global.dto.res.ApiRes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/post")
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ApiRes<Long> createPost(@RequestPart PostCreate dto,
                                   @RequestPart List<MultipartFile> images,
                                   @AuthenticationPrincipal Member member){
        return new ApiRes<>(postService.savePost(dto,images,member));
    }
}
