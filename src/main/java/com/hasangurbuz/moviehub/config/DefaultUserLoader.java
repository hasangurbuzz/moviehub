package com.hasangurbuz.moviehub.config;

import com.hasangurbuz.moviehub.domain.Permission;
import com.hasangurbuz.moviehub.domain.Role;
import com.hasangurbuz.moviehub.domain.User;
import com.hasangurbuz.moviehub.service.PermissionService;
import com.hasangurbuz.moviehub.service.RoleService;
import com.hasangurbuz.moviehub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.hasangurbuz.moviehub.domain.PermissionType.READ;
import static com.hasangurbuz.moviehub.domain.PermissionType.WRITE;

@Component
@RequiredArgsConstructor
public class DefaultUserLoader implements CommandLineRunner {

    private final PermissionService permissionService;

    private final UserService userService;

    private final BCryptPasswordEncoder encoder;

    private final RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        Permission write = permissionService.findByType(WRITE);
        if (write == null) {
            write = new Permission();
            write.setType(WRITE);
            write = permissionService.create(write);
        }
        Permission read = permissionService.findByType(READ);
        if (read == null) {
            read = new Permission();
            read.setType(READ);
            read = permissionService.create(read);
        }

        if (userService.existUsername("hasangurbuz")){
            return;
        }

        Role admin = roleService.findByName("ADMIN");
        if (admin == null) {
            admin = new Role();
            admin.setName("ADMIN");
            admin.setPermissions(Arrays.asList(read, write));
            admin = roleService.create(admin);
        }
        User user = new User();
        user.setName("hasangurbuz");
        user.setPassword(encoder.encode("1234"));
        user.setRole(admin);
        userService.create(user);
    }
}
