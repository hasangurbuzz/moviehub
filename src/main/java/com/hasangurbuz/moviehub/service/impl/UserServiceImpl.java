package com.hasangurbuz.moviehub.service.impl;

import com.hasangurbuz.moviehub.domain.QUser;
import com.hasangurbuz.moviehub.domain.User;
import com.hasangurbuz.moviehub.repository.UserRepository;
import com.hasangurbuz.moviehub.service.UserService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final QUser qUser = QUser.user;

    private final JPAQueryFactory query;

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return query.selectFrom(qUser)
                .where(qUser.id.eq(id))
                .fetchOne();
    }

    @Override
    public boolean existUsername(String username) {
        long count = query.selectFrom(qUser)
                .where(qUser.name.eq(username))
                .fetchCount();

        return count > 0;
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }
}
