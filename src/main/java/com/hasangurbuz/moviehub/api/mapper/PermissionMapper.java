package com.hasangurbuz.moviehub.api.mapper;

import com.hasangurbuz.moviehub.domain.Permission;
import com.hasangurbuz.moviehub.domain.PermissionType;
import org.openapitools.model.PermissionDTO;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper implements Mapper<Permission, PermissionDTO> {

    @Override
    public Permission toEntity(PermissionDTO dto) {
        Permission permission = new Permission();
        permission.setType(PermissionType.valueOf(dto.getValue()));
        return permission;
    }

    @Override
    public PermissionDTO toDto(Permission permission) {
        return PermissionDTO.valueOf(permission.getType().name());
    }

}
