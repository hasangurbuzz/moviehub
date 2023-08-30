package com.hasangurbuz.moviehub.api.mapper;

import com.hasangurbuz.moviehub.domain.Movie;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.model.MovieDTO;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper implements Mapper<Movie, MovieDTO> {

    @Override
    public Movie toEntity(MovieDTO dto) {
        Movie movie = new Movie();
        movie.setTitle(StringUtils.normalizeSpace(dto.getTitle()));
        movie.setDescription(StringUtils.normalizeSpace(dto.getDescription()));
        movie.setDirector(StringUtils.normalizeSpace(dto.getDirector()));
        return movie;
    }

    @Override
    public MovieDTO toDto(Movie movie) {
        MovieDTO dto = new MovieDTO();
        dto.setId(movie.getId());
        dto.setDescription(movie.getDescription());
        dto.setDirector(movie.getDirector());
        dto.setTitle(movie.getTitle());
        return dto;
    }

}
