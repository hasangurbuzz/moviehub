package com.hasangurbuz.moviehub.service.impl;

import com.hasangurbuz.moviehub.domain.Movie;
import com.hasangurbuz.moviehub.domain.QMovie;
import com.hasangurbuz.moviehub.repository.MovieRepository;
import com.hasangurbuz.moviehub.service.MovieService;
import com.hasangurbuz.moviehub.service.PagedResult;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    private final JPAQueryFactory query;

    private final QMovie qMovie = QMovie.movie;

    @Override
    public Movie create(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public PagedResult<Movie> search(String term, PageRequest pageRequest) {
        QueryResults<Movie> results = query.selectFrom(qMovie)
                .where(qMovie.title.containsIgnoreCase(term))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetchResults();

        PagedResult<Movie> pagedResult = new PagedResult<>();
        pagedResult.setTotal(results.getTotal());
        pagedResult.setResults(results.getResults());
        return pagedResult;
    }

    @Override
    public boolean existTitle(String title) {
        long count = query.selectFrom(qMovie)
                .where(qMovie.title.equalsIgnoreCase(title))
                .fetchCount();

        return count > 0;
    }

    @Override
    public Movie findById(Long movieId) {
        return query.selectFrom(qMovie)
                .where(qMovie.id.eq(movieId))
                .fetchOne();
    }
}
