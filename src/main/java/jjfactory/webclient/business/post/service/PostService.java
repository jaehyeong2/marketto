package jjfactory.webclient.business.post.service;

import jjfactory.webclient.business.category.domain.Category;
import jjfactory.webclient.business.category.repository.CategoryRepository;
import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.post.domain.Post;
import jjfactory.webclient.business.post.domain.PostLike;
import jjfactory.webclient.business.post.domain.report.Report;
import jjfactory.webclient.business.post.dto.req.PostCreate;
import jjfactory.webclient.business.post.dto.req.PostUpdate;
import jjfactory.webclient.business.post.dto.req.ReportCreate;
import jjfactory.webclient.business.post.dto.res.PostDetailRes;
import jjfactory.webclient.business.post.dto.res.PostRes;
import jjfactory.webclient.business.post.repository.PostLikeRepository;
import jjfactory.webclient.business.post.repository.PostQueryRepository;
import jjfactory.webclient.business.post.repository.PostRepository;
import jjfactory.webclient.business.post.repository.ReportRepository;
import jjfactory.webclient.global.dto.req.FcmMessageDto;
import jjfactory.webclient.global.dto.res.PagingRes;
import jjfactory.webclient.global.ex.BusinessException;
import jjfactory.webclient.global.ex.ErrorCode;
import jjfactory.webclient.global.slack.SlackService;
import jjfactory.webclient.global.util.FireBasePush;
import jjfactory.webclient.global.util.image.S3Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class PostService {
    public static final String POST_IMAGE_PATH = "postImages/";
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CategoryRepository categoryRepository;
    private final PostQueryRepository postQueryRepository;
    private final ReportRepository reportRepository;
    private final S3Upload s3Upload;
    private final FireBasePush fireBasePush;

    @Transactional(readOnly = true)
    public PagingRes<PostRes> findAllPosts(Pageable pageable,String startDate,String endDate,String query,Boolean orderType){
        return new PagingRes<>(postQueryRepository.findAllPosts(pageable,startDate,endDate,query,orderType));
    }

    @Transactional(readOnly = true)
    public PagingRes<PostRes> findMyPosts(Pageable pageable,String startDate,String endDate,Member member){
        return new PagingRes<>(postQueryRepository.findMyPosts(pageable,startDate,endDate,member));
    }

    @Transactional(readOnly = true)
    public PostDetailRes findPost(Long postId){
        return postQueryRepository.findPost(postId);
    }

    public Long savePost(PostCreate dto, List<MultipartFile> images, Member member){
        Category category = getCategory(dto.getCategoryId());

        Post post = Post.create(dto, member, category);
        post.addImagePaths(addImages(images));
        postRepository.save(post);
        return post.getId();
    }

    public Long like(Member loginMember,Long postId){
        Post post = getPost(postId);
        PostLike postLike = PostLike.create(loginMember, post);

        postLikeRepository.save(postLike);
        fireBasePush.sendMessage(FcmMessageDto.builder()
                        .fcmToken(post.getMember().getFcmToken())
                        .title(loginMember.getUsername()+"님이 회원님의 게시물에 좋아요를 눌렀습니다")
                .build());

        return postLike.getId();
    }

    public Long report(Member loginMember, ReportCreate dto){
        Post post = getPost(dto.getPostId());
        Report report = Report.create(loginMember, post, dto.getReason());

        reportRepository.save(report);
        fireBasePush.sendMessage(FcmMessageDto.builder()
                .fcmToken(post.getMember().getFcmToken())
                .title("회원님의 게시물이 신고를 부적절한 컨텐츠(내용)로 신고당했습니다.")
                .build());

        return report.getId();
    }

    private List<String> addImages(List<MultipartFile> files) {
        return files.stream()
                .map(f -> s3Upload.upload(f, POST_IMAGE_PATH))
                .collect(Collectors.toList());
    }

    public Long update(PostUpdate req, Long postId, Member loginMember) {
        Post findPost = getPost(postId);
        memberValidate(loginMember, findPost);

        findPost.update(req);
        return findPost.getId();
    }

    public String deleteById(Long postId, Member loginMember) {
        Post findPost = getPost(postId);
        memberValidate(loginMember, findPost);

        postRepository.deleteById(findPost.getId());
        return "ok";
    }

    private void memberValidate(Member member, Post findPost) {
        if(!findPost.getMember().equals(member)){
            throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
        }
    }

    private Post getPost(Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow(() ->{
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
        });
        return findPost;
    }
    private Category getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
        });
        return category;
    }
}
