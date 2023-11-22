package be.equals.authservice.service;

import be.equals.authservice.domain.User;

public interface UserService {
    boolean exists(Long id);
    void create(User user);
    User getPrincipalUser();
}
