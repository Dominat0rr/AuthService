package be.equals.authservice.repository;

import be.equals.authservice.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Sql(UserRepositoryIntegrationTest.INIT_DB_SCRIPT)
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@EntityScan(basePackageClasses = User.class)
class UserRepositoryIntegrationTest extends MySQLTestContainer {
    protected static final String INIT_DB_SCRIPT = "classpath:users.sql";

    @Autowired
    private UserRepository userRepository;

    private static final String EXISTING_EMAIL = "john@email.be";
    private static final String NON_EXISTING_EMAIL = "test@email.be";

    @Test
    void contextLoads() {
        assertThat(userRepository).isNotNull();
    }

    @Test
    void whenFindByEmail_withExistingEmail_thenUserIsReturned() {
        User expectedUser = createUser();

        Optional<User> user = userRepository.findByEmail(EXISTING_EMAIL);

        assertThat(user.get())
                .usingRecursiveComparison()
                .ignoringFields("password")
                .isEqualTo(expectedUser);
    }

    @Test
    void whenFindByEmail_withNonExsitingEmail_ThenOptionalEmptyIsReturned() {
        Optional<User> user = userRepository.findByEmail(NON_EXISTING_EMAIL);

        assertThat(user).isEqualTo(Optional.empty());
    }

    private static User createUser() {
        return User.builder()
                .id(100L)
                .email("john@email.be")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .username("john")
                .build();
    }
}
