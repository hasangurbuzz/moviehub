package com.hasangurbuz.moviehub.api.mapper;

import com.hasangurbuz.moviehub.domain.Role;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.RoleDTO;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleMapper implements Mapper<Role, RoleDTO> {

    private final PermissionMapper permissionMapper;

    @Override
    public Role toEntity(RoleDTO dto) {
        Role role = new Role();
        role.setName(dto.getName());
        return role;
    }

    @Override
    public RoleDTO toDto(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setName(role.getName());
        dto.setId(role.getId());
        dto.setPermissions(permissionMapper.toDtoList(role.getPermissions()));
        return dto;
    }

}
