package com.hasangurbuz.moviehub.service;

import com.hasangurbuz.moviehub.domain.Comment;
import org.springframework.data.domain.PageRequest;

public interface CommentService {
    Comment create(Comment comment);

    PagedResult<Comment> search(Long movieId, PageRequest pageRequest);

    void delete(Comment comment);

    Comment findById(Long commentId);
}
