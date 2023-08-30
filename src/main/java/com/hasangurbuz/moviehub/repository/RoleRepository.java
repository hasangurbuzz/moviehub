package com.hasangurbuz.moviehub.repository;

import com.hasangurbuz.moviehub.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
