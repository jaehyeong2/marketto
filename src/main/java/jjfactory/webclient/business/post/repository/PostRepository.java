package jjfactory.webclient.business.post.repository;

import jjfactory.webclient.business.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
