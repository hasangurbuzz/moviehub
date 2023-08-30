package com.hasangurbuz.moviehub.api.mapper;

import com.hasangurbuz.moviehub.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.CommentDTO;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper implements Mapper<Comment, CommentDTO> {

    private final UserMapper userMapper;

    @Override
    public Comment toEntity(CommentDTO dto) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        return comment;
    }

    @Override
    public CommentDTO toDto(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setUser(userMapper.toDto(comment.getUser()));
        return dto;
    }
}
