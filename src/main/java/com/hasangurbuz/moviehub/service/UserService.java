package com.hasangurbuz.moviehub.service;

import com.hasangurbuz.moviehub.domain.User;

public interface UserService {

    User create(User user);

    User findById(Long id);

    boolean existUsername(String username);

    User update(User user);
}
