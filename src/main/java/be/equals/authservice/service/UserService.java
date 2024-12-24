package be.equals.authservice.service;

import be.equals.authservice.domain.User;

public interface UserService {
    boolean exists(Long id);
    boolean existsByEmail(String email);
    void create(User user);
    User getPrincipalUser();
}
