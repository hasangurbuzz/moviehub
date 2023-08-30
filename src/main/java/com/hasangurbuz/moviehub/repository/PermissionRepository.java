package com.hasangurbuz.moviehub.repository;

import com.hasangurbuz.moviehub.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}