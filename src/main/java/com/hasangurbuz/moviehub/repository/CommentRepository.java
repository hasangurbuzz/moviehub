package com.hasangurbuz.moviehub.repository;

import com.hasangurbuz.moviehub.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}