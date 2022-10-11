package jjfactory.webclient.business.comment.repository;

import jjfactory.webclient.business.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
