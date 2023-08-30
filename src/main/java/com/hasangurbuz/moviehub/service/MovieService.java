package com.hasangurbuz.moviehub.service;

import com.hasangurbuz.moviehub.domain.Movie;
import org.springframework.data.domain.PageRequest;

public interface MovieService {
    Movie create(Movie movie);

    PagedResult<Movie> search(String term, PageRequest pageRequest);

    boolean existTitle(String title);

    Movie findById(Long movieId);
}
