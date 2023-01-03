package dpimkin.drone.fleet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static java.lang.Boolean.TRUE;

@Slf4j
@Testcontainers
public abstract class DockerizedTestSupport {
    private static final String POSTGRESQL_IMAGE = "postgres:13.7-alpine";

    @Container
    final static PostgreSQLContainer<?> POSTGRES = createDatabaseInstance();

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        // region Flyway
        registry.add("spring.flyway.url", DockerizedTestSupport::getPostgresqlUrlForFlyway);
        registry.add("spring.flyway.user", POSTGRES::getUsername);
        registry.add("spring.flyway.password", POSTGRES::getPassword);
        registry.add("spring.flyway.driver-class-name", POSTGRES::getDriverClassName);
        // endregion


        // region R2DBC
        registry.add("spring.r2dbc.url", DockerizedTestSupport::getPostgresqlUrlForR2dbc);
        registry.add("spring.r2dbc.username", POSTGRES::getUsername);
        registry.add("spring.r2dbc.password", POSTGRES::getPassword);
        // endregion
    }

    public static PostgreSQLContainer<?> createDatabaseInstance() {
        int containerPort = 5432 ;
        int localPort = 5432 ;
        PostgreSQLContainer<?> container = new PostgreSQLContainer(POSTGRESQL_IMAGE) {{
            if (TRUE.toString().equalsIgnoreCase(System.getProperty("DEBUG"))) {
                this.withDatabaseName("db").withUsername("user").withPassword("1q2w3e");
                this.addFixedExposedPort(localPort, containerPort);
            }
        }};
        return container;
    }


    private static String getPostgresqlUrlForFlyway() {
        var result = POSTGRES.getJdbcUrl();
        log.info("spring.flyway.url={}", result);
        return result;
    }

    private static String getPostgresqlUrlForR2dbc() {
        var result = POSTGRES.getJdbcUrl().replace("jdbc", "r2dbc:pool");
        log.info("spring.r2dbc.url={}", result);
        return result;
    }

}
