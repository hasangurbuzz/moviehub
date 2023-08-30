package com.hasangurbuz.moviehub.api;

import com.hasangurbuz.moviehub.api.mapper.PageRequestMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openapitools.model.PageRequestDTO;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

import static com.hasangurbuz.moviehub.api.ApiConstant.PAGEREQUEST_DEFAULT_OFFSET;
import static com.hasangurbuz.moviehub.api.ApiConstant.PAGEREQUEST_DEFAULT_SIZE;

@Component
@RequiredArgsConstructor
public class ApiUtils {

    public void validate(Object object) {
        Field[] fields = FieldUtils.getAllFields(object.getClass());

        for (Field field : fields) {
            if (field.getType() != String.class) {
                continue;
            }
            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);
            String fieldName = field.getName();
            try {
                Object value = field.get(object);
                field.setAccessible(isAccessible);
                if (StringUtils.isBlank(value.toString())) {
                    throw ApiException.invalidInput(fieldName + " required");
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public PageRequestDTO normalizePageRequest(PageRequestDTO pageRequestDTO) {
        if (pageRequestDTO.getFrom() == null) {
            pageRequestDTO.setFrom(PAGEREQUEST_DEFAULT_OFFSET);
        }
        if (pageRequestDTO.getFrom() < 0) {
            pageRequestDTO.setFrom(PAGEREQUEST_DEFAULT_OFFSET);
        }
        if (pageRequestDTO.getFrom() >= pageRequestDTO.getSize()) {
            pageRequestDTO.setFrom(PAGEREQUEST_DEFAULT_OFFSET);
            pageRequestDTO.setSize(PAGEREQUEST_DEFAULT_SIZE);
        }

        return pageRequestDTO;
    }
}
