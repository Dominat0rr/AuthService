package be.equals.authservice;

import be.equals.authservice.domain.User;
import be.equals.authservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Slf4j
@Profile({"local", "dev"})
public class TestConfiguration {
    private final UserService userService;

    List<User> users = List.of(
            User.builder()
                    .id(100L)
                    .email("john@email.be")
                    .username("john")
                    .firstName("John")
                    .lastName("Doe")
                    .password("password")
                    .build(),
            User.builder()
                    .id(101L)
                    .email("jane@email.be")
                    .username("jane")
                    .firstName("Jane")
                    .lastName("Doe")
                    .password("password")
                    .build()
    );

    public TestConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public void insertTestData() {
        log.info("inserting users test data...");

        users.forEach(user -> {
            if (!userService.exists(user.getId()))
                userService.create(user);
        });
    }
}
