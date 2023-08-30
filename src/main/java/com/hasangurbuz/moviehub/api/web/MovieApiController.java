package com.hasangurbuz.moviehub.api.web;

import com.hasangurbuz.moviehub.api.ApiException;
import com.hasangurbuz.moviehub.api.ApiUtils;
import com.hasangurbuz.moviehub.api.mapper.CommentMapper;
import com.hasangurbuz.moviehub.api.mapper.MovieMapper;
import com.hasangurbuz.moviehub.api.mapper.PageRequestMapper;
import com.hasangurbuz.moviehub.domain.Comment;
import com.hasangurbuz.moviehub.domain.Movie;
import com.hasangurbuz.moviehub.domain.User;
import com.hasangurbuz.moviehub.security.UserDetail;
import com.hasangurbuz.moviehub.service.CommentService;
import com.hasangurbuz.moviehub.service.MovieService;
import com.hasangurbuz.moviehub.service.PagedResult;
import com.hasangurbuz.moviehub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.MovieApi;
import org.openapitools.model.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MovieApiController implements MovieApi {

    private final ApiUtils apiUtils;

    private final MovieService movieService;

    private final MovieMapper movieMapper;

    private final PageRequestMapper pageRequestMapper;

    private final CommentService commentService;

    private final UserService userService;

    private final CommentMapper commentMapper;


    @Override
    @Transactional(readOnly = true)
//    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<MovieSearchResponseDTO> search(MovieSearchRequestDTO movieSearchRequestDTO) {
        apiUtils.validate(movieSearchRequestDTO);
        PageRequest pageRequest = pageRequestMapper.toEntity(movieSearchRequestDTO.getPageRequest());

        PagedResult<Movie> results = movieService.search(movieSearchRequestDTO.getTitle(), pageRequest);

        MovieSearchResponseDTO response = new MovieSearchResponseDTO();
        response.setItems(movieMapper.toDtoList(results.getResults()));
        response.setTotal(results.getTotal());

        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('WRITE')")
    public ResponseEntity<MovieDTO> create(MovieCreateRequestDTO movieCreateRequestDTO) {
        apiUtils.validate(movieCreateRequestDTO);
        boolean existsTitle = movieService.existTitle(movieCreateRequestDTO.getTitle());
        if (existsTitle) {
            throw ApiException.invalidInput("Existing title : " + movieCreateRequestDTO.getTitle());
        }

        Movie movie = new Movie();
        movie.setTitle(movieCreateRequestDTO.getTitle());
        movie.setDescription(movieCreateRequestDTO.getDescription());
        movie.setDirector(movieCreateRequestDTO.getDirector());

        movie = movieService.create(movie);

        return ResponseEntity.ok(movieMapper.toDto(movie));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('WRITE')")
    public ResponseEntity<CommentDTO> createComment(Long movieId, CommentCreateRequestDTO commentCreateRequestDTO) {
        apiUtils.validate(commentCreateRequestDTO);

        Movie movie = movieService.findById(movieId);
        if (movie == null) {
            throw ApiException.notFound("Not found with id : " + movieId);
        }

        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findById(userDetail.getId());

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setMovie(movie);
        comment.setContent(commentCreateRequestDTO.getContent());
        comment = commentService.create(comment);
        CommentDTO response = commentMapper.toDto(comment);

        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<CommentSearchResponseDTO> searchComment(Long movieId, PageRequestDTO pageRequestDTO) {

        Movie movie = movieService.findById(movieId);
        if (movie == null) {
            throw ApiException.notFound("Not found with id : " + movieId);
        }

        PageRequest pageRequest = pageRequestMapper.toEntity(pageRequestDTO);

        PagedResult<Comment> results = commentService.search(movieId, pageRequest);

        CommentSearchResponseDTO response = new CommentSearchResponseDTO();
        response.setTotal(results.getTotal());
        response.setItems(commentMapper.toDtoList(results.getResults()));

        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('WRITE')")
    public ResponseEntity<Void> deleteComment(Long movieId, Long commentId) {
        Movie movie = movieService.findById(movieId);
        if (movie == null) {
            throw ApiException.notFound("Not found with movieId" + movieId);
        }

        Comment comment = commentService.findById(commentId);

        if (comment == null) {
            throw ApiException.notFound("Not found with commentId" + commentId);
        }

        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetail.getId() != comment.getUser().getId()) {
            throw ApiException.notFound("Not found with id : " + commentId);
        }

        commentService.delete(comment);

        return ResponseEntity.ok().build();
    }
}
