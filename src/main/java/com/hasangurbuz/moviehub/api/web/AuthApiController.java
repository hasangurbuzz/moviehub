package com.hasangurbuz.moviehub.api.web;

import com.hasangurbuz.moviehub.api.ApiException;
import com.hasangurbuz.moviehub.api.ApiUtils;
import com.hasangurbuz.moviehub.api.mapper.UserMapper;
import com.hasangurbuz.moviehub.domain.Role;
import com.hasangurbuz.moviehub.domain.User;
import com.hasangurbuz.moviehub.security.JwtService;
import com.hasangurbuz.moviehub.security.UserDetail;
import com.hasangurbuz.moviehub.service.RoleService;
import com.hasangurbuz.moviehub.service.UserService;
import com.hasangurbuz.moviehub.service.impl.UserDetailServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.AuthApi;
import org.openapitools.model.AuthRequestDTO;
import org.openapitools.model.JwtTokenDTO;
import org.openapitools.model.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneId;

import static com.hasangurbuz.moviehub.api.ApiConstant.ROLE_DEFAULT;

@RestController
@RequiredArgsConstructor
public class AuthApiController implements AuthApi {

    private final UserService userService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authManager;

    private final UserDetailServiceImpl userDetailService;

    private final RoleService roleService;

    private final ApiUtils apiUtils;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public ResponseEntity<UserDTO> register(AuthRequestDTO authRequestDTO) {
        apiUtils.validate(authRequestDTO);

        if (userService.existUsername(authRequestDTO.getUsername())) {
            throw ApiException.invalidInput("Username exists");
        }

        Role role = roleService.findByName(ROLE_DEFAULT);
        if (role == null) {
            role = new Role();
            role.setName(ROLE_DEFAULT);
            role = roleService.create(role);
        }

        User user = new User();
        user.setName(authRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(authRequestDTO.getPassword()));
        user.setRole(role);

        user = userService.create(user);

        UserDTO response = userMapper.toDto(user);

        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<JwtTokenDTO> login(AuthRequestDTO authRequestDTO) {
        apiUtils.validate(authRequestDTO);

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDTO.getUsername(),
                        authRequestDTO.getPassword()
                )
        );

        UserDetail userDetail = (UserDetail) authentication.getPrincipal();

        String jwtToken = jwtService.generateToken(userDetail.getUsername());
        OffsetDateTime expireDate = jwtService
                .extractClaim(jwtToken, Claims::getExpiration)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toOffsetDateTime();

        JwtTokenDTO token = new JwtTokenDTO();
        token.setValue(jwtToken);
        token.setExpiration(expireDate);
        return ResponseEntity.ok(token);
    }



}
