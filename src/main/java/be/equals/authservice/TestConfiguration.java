package be.equals.authservice;

import be.equals.authservice.domain.User;
import be.equals.authservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Slf4j
@Profile({"local", "dev"})
public class TestConfiguration {
    private final UserService userService;

    public TestConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> initializeTestUsers();
    }

    private void initializeTestUsers() {
        List<User> users = List.of(
                User.builder()
                        .email("john@email.be")
                        .username("john")
                        .firstName("John")
                        .lastName("Doe")
                        .password("password")
                        .build(),
                User.builder()
                        .email("jane@email.be")
                        .username("jane")
                        .firstName("Jane")
                        .lastName("Doe")
                        .password("password")
                        .build()
        );

        users.forEach(user -> {
            if (!userService.existsByEmail(user.getEmail())) {
                userService.create(user);
                log.info("User created: {}", user.getUsername());
            } else {
                log.info("User already exists: {}", user.getUsername());
            }
        });
    }
}