package com.hasangurbuz.moviehub.service.impl;

import com.hasangurbuz.moviehub.domain.QRole;
import com.hasangurbuz.moviehub.domain.Role;
import com.hasangurbuz.moviehub.repository.RoleRepository;
import com.hasangurbuz.moviehub.service.PagedResult;
import com.hasangurbuz.moviehub.service.RoleService;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final JPAQueryFactory query;

    private final QRole qRole = QRole.role;

    @Override
    public Role create(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role findByName(String name) {
        Role role = query.selectFrom(qRole)
                .where(qRole.name.eq(name))
                .fetchOne();

        return role;
    }

    @Override
    public PagedResult<Role> search(String term, PageRequest pageRequest) {
        QueryResults<Role> results = query.selectFrom(qRole)
                .where(
                        qRole.name.containsIgnoreCase(term)
                )
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetchResults();

        PagedResult<Role> roles = new PagedResult<>();
        roles.setResults(results.getResults());
        roles.setTotal(results.getTotal());
        return roles;
    }

    @Override
    public void delete(Role role) {
        roleRepository.delete(role);
    }

    @Override
    public Role findById(Long id) {
        return query.selectFrom(qRole)
                .where(qRole.id.eq(id))
                .fetchOne();
    }
}
