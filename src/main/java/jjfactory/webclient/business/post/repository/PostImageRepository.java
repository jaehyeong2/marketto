package jjfactory.webclient.business.post.repository;

import jjfactory.webclient.business.post.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage,Long> {
}
