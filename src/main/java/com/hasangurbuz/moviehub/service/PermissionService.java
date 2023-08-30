package com.hasangurbuz.moviehub.service;

import com.hasangurbuz.moviehub.domain.Permission;
import com.hasangurbuz.moviehub.domain.PermissionType;

public interface PermissionService {
    Permission create(Permission permission);

    Permission findByType(PermissionType type);
}
