package jjfactory.webclient.business.post.service;

import jjfactory.webclient.business.category.domain.Category;
import jjfactory.webclient.business.category.repository.CategoryRepository;
import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.post.domain.Post;
import jjfactory.webclient.business.post.dto.req.PostCreate;
import jjfactory.webclient.business.post.dto.req.PostUpdate;
import jjfactory.webclient.business.post.dto.res.PostRes;
import jjfactory.webclient.business.post.repository.PostQueryRepository;
import jjfactory.webclient.business.post.repository.PostRepository;
import jjfactory.webclient.global.dto.res.PagingRes;
import jjfactory.webclient.global.ex.BusinessException;
import jjfactory.webclient.global.ex.ErrorCode;
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
    private final CategoryRepository categoryRepository;
    private final PostQueryRepository postQueryRepository;
    private final S3Upload s3Upload;
    @Transactional(readOnly = true)
    public PagingRes<PostRes> findAllPosts(Pageable pageable,String startDate,String endDate,String query){
        return new PagingRes<>(postQueryRepository.findAllPosts(pageable,startDate,endDate,query));
    }
    public Long savePost(PostCreate dto, List<MultipartFile> images, Member member){
        Category category = getCategory(dto.getCategoryId());

        Post post = Post.create(dto, member, category);
        post.addImagePaths(addImages(images));
        postRepository.save(post);
        return post.getId();
    }

    private List<String> addImages(List<MultipartFile> files) {
        return files.stream()
                .map(f -> s3Upload.upload(f, POST_IMAGE_PATH))
                .collect(Collectors.toList());
    }

    public void update(PostUpdate req, Long postId, Member member) {
        Post findPost = getPost(postId);
        findPost.update(req);
    }

    public void deleteById(Long postId, Member member) {
        Post findPost = getPost(postId);
        postRepository.deleteById(findPost.getId());
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
