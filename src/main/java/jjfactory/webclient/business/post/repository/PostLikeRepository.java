package jjfactory.webclient.business.post.repository;

import jjfactory.webclient.business.post.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
}
