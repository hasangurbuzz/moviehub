package com.hasangurbuz.moviehub.api.mapper;

import com.hasangurbuz.moviehub.api.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.PageRequestDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageRequestMapper implements Mapper<PageRequest, PageRequestDTO> {

    private final ApiUtils apiUtils;

    @Override
    public PageRequest toEntity(PageRequestDTO dto) {
        dto = apiUtils.normalizePageRequest(dto);
        return PageRequest.of(dto.getFrom() / dto.getSize(), dto.getSize());
    }

    @Override
    public PageRequestDTO toDto(PageRequest pageRequest) {
        return null;
    }

}
