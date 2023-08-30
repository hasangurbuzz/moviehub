package com.hasangurbuz.moviehub.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PublicEndpoint implements InitializingBean {
    @Value("${server.servlet.context-path}")
    private String contextPath;
    private String AUTH_REGISTER = "/auth/register";
    private String AUTH_LOGIN = "/auth/login";
    private String AUTH_OAUTH = "/login";
    private Set<String> ALL = new HashSet<>();


    @Override
    public void afterPropertiesSet() throws Exception {
        add(AUTH_REGISTER);
        add(AUTH_LOGIN);
        add(AUTH_OAUTH);
    }

    private void add(String endpoint) {
        ALL.add(endpoint);
    }

    public Set<String> getAll(boolean withContextPath) {
        if (withContextPath) {
            return ALL.stream()
                    .map(endpoint -> contextPath + endpoint)
                    .collect(Collectors.toSet());
        }
        return ALL;
    }

}
