package com.hasangurbuz.moviehub.api.web;

import com.hasangurbuz.moviehub.api.ApiException;
import com.hasangurbuz.moviehub.api.ApiUtils;
import com.hasangurbuz.moviehub.api.mapper.PageRequestMapper;
import com.hasangurbuz.moviehub.api.mapper.PermissionMapper;
import com.hasangurbuz.moviehub.api.mapper.RoleMapper;
import com.hasangurbuz.moviehub.domain.Permission;
import com.hasangurbuz.moviehub.domain.Role;
import com.hasangurbuz.moviehub.domain.User;
import com.hasangurbuz.moviehub.service.PagedResult;
import com.hasangurbuz.moviehub.service.PermissionService;
import com.hasangurbuz.moviehub.service.RoleService;
import com.hasangurbuz.moviehub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.api.ManagementApi;
import org.openapitools.model.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') and hasAuthority('WRITE')")
public class ManagementApiController implements ManagementApi {

    private final RoleService roleService;

    private final RoleMapper roleMapper;

    private final PermissionService permissionService;

    private final PermissionMapper permissionMapper;

    private final UserService userService;

    private final ApiUtils apiUtils;

    private final PageRequestMapper pageRequestMapper;

    @Override
    @Transactional
    public ResponseEntity<RoleDTO> createRole(RoleCreateRequestDTO roleCreateRequestDTO) {
        apiUtils.validate(roleCreateRequestDTO);
        roleCreateRequestDTO.setName(StringUtils.upperCase(roleCreateRequestDTO.getName()));
        Role role = roleService.findByName(roleCreateRequestDTO.getName());
        if (role != null) {
            throw ApiException.invalidInput("Name exists");
        }

        role = new Role();
        role.setName(roleCreateRequestDTO.getName());
        Set<Permission> permissions = new HashSet<>();

        for (PermissionDTO permission : roleCreateRequestDTO.getPermissions()) {
            Permission p = permissionService.findByType(permissionMapper.toEntity(permission).getType());
            if (p == null) {
                throw ApiException.notFound("Not found : " + permission);
            }
            permissions.add(p);
        }

        role.setPermissions(permissions.stream().collect(Collectors.toList()));
        role = roleService.create(role);

        RoleDTO response = roleMapper.toDto(role);
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<RoleSearchResponseDTO> searchRole(RoleSearchRequestDTO roleSearchRequestDTO) {
        apiUtils.validate(roleSearchRequestDTO);
        PageRequest pageRequest = pageRequestMapper.toEntity(roleSearchRequestDTO.getPageRequest());

        PagedResult<Role> result = roleService.search(roleSearchRequestDTO.getName(), pageRequest);

        RoleSearchResponseDTO response = new RoleSearchResponseDTO();
        response.setItems(roleMapper.toDtoList(result.getResults()));
        response.setTotal(result.getTotal());
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteRole(Long roleId) {
        Role role = roleService.findById(roleId);
        if (role == null) {
            throw ApiException.notFound("Not found with id : " + roleId);
        }

        roleService.delete(role);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<RoleDTO> modifyUserRole(Long userId, UserUpdateRoleRequestDTO userUpdateRoleRequestDTO) {
        apiUtils.validate(userUpdateRoleRequestDTO);
        userUpdateRoleRequestDTO.setName(StringUtils.upperCase(userUpdateRoleRequestDTO.getName()));
        User user = userService.findById(userId);
        if (user == null) {
            throw ApiException.notFound("Not found with id : " + userId);
        }

        Role role = roleService.findByName(userUpdateRoleRequestDTO.getName());
        if (role == null) {
            throw ApiException.notFound("Not found with name : " + userUpdateRoleRequestDTO.getName());
        }

        user.setRole(role);
        user = userService.update(user);

        RoleDTO response = roleMapper.toDto(role);

        return ResponseEntity.ok(response);
    }

}
