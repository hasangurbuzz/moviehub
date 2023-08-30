package com.hasangurbuz.moviehub.service.impl;

import com.hasangurbuz.moviehub.domain.Permission;
import com.hasangurbuz.moviehub.domain.PermissionType;
import com.hasangurbuz.moviehub.domain.QPermission;
import com.hasangurbuz.moviehub.repository.PermissionRepository;
import com.hasangurbuz.moviehub.service.PermissionService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    private final JPAQueryFactory query;

    @Override
    public Permission create(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Permission findByType(PermissionType type) {
        QPermission qPerm = QPermission.permission;
        Permission permission = query.selectFrom(qPerm)
                .where(qPerm.type.eq(type))
                .fetchOne();

        return permission;
    }
}
