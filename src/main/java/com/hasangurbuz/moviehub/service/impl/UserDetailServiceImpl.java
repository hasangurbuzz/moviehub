package com.hasangurbuz.moviehub.service.impl;

import com.hasangurbuz.moviehub.domain.Permission;
import com.hasangurbuz.moviehub.domain.QUser;
import com.hasangurbuz.moviehub.domain.User;
import com.hasangurbuz.moviehub.repository.UserRepository;
import com.hasangurbuz.moviehub.security.UserDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;

    private final JPAQueryFactory query;

    private final QUser qUser = QUser.user;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = query.selectFrom(qUser)
                .where(qUser.name.eq(username))
                .fetchOne();

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        UserDetail userDetail = new UserDetail();
        userDetail.setId(user.getId());
        userDetail.setUsername(user.getName());
        userDetail.setPassword(user.getPassword());
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
        for (Permission permission : user.getRole().getPermissions()) {
            authorityList.add(new SimpleGrantedAuthority(permission.getType().toString()));
        }
        userDetail.setAuthorities(authorityList);

        return userDetail;
    }
}
