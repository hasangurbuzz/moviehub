package com.hasangurbuz.moviehub.service;

import com.hasangurbuz.moviehub.domain.Role;
import org.springframework.data.domain.PageRequest;

public interface RoleService {
    Role create(Role role);

    Role findByName(String name);

    PagedResult<Role> search(String term, PageRequest pageRequest);

    void delete(Role role);

    Role findById(Long id);
}
