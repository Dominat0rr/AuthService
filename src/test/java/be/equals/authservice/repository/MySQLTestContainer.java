package be.equals.authservice.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"repository-test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@EnableJpaRepositories(basePackages = {"be.equals.repository"})
public abstract class MySQLTestContainer {
    protected static MySQLContainer<?> MYSQL_SERVER_CONTAINER;
    private static final String MY_SQL_DOCKER_CONTAINER_IMAGE = "mysql:8.0.28";

    public MySQLTestContainer() { }

    @AfterAll
    static void afterAll() {
        MYSQL_SERVER_CONTAINER.stop();
    }

    @BeforeAll
    static void beforeAll() {
        MYSQL_SERVER_CONTAINER = (new MySQLContainer<>(DockerImageName.parse(MY_SQL_DOCKER_CONTAINER_IMAGE)))
                .withUsername("test-user")
                .withPassword("test-pwd")
                .withDatabaseName("test-db");
        MYSQL_SERVER_CONTAINER.start();
    }

    @Test
    void testContainerRunning() {
        assertThat(MYSQL_SERVER_CONTAINER.isRunning()).isTrue();
    }
}
