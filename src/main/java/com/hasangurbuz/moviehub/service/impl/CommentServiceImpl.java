package com.hasangurbuz.moviehub.service.impl;

import com.hasangurbuz.moviehub.domain.Comment;
import com.hasangurbuz.moviehub.domain.QComment;
import com.hasangurbuz.moviehub.repository.CommentRepository;
import com.hasangurbuz.moviehub.service.CommentService;
import com.hasangurbuz.moviehub.service.PagedResult;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final JPAQueryFactory query;

    private final QComment qComment = QComment.comment;

    @Override
    public Comment create(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public PagedResult<Comment> search(Long movieId, PageRequest pageRequest) {
        QueryResults<Comment> commentQueryResults = query.selectFrom(qComment)
                .where(qComment.movie.id.eq(movieId))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetchResults();

        PagedResult<Comment> result = new PagedResult<>();
        result.setResults(commentQueryResults.getResults());
        result.setTotal(commentQueryResults.getTotal());
        return result;
    }

    @Override
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    public Comment findById(Long commentId) {
        return query.selectFrom(qComment)
                .where(qComment.id.eq(commentId))
                .fetchOne();
    }
}
