package dpimkin.drone.fleet.persistence;

import dpimkin.drone.fleet.domain.DroneModelType;
import dpimkin.drone.fleet.domain.DroneState;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import static java.lang.Boolean.TRUE;
import static java.time.Instant.now;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = {
        "logging.level.io.r2dbc.postgresql.QUERY=DEBUG",
        "logging.level.io.r2dbc.postgresql.PARAM=DEBUG"})
class PreconditionsTest {
    private static final String SAME_SERIAL_NUMBER ="1101";
    private static final String SAME_IDEMPOTENCY_KEY = "foobar";

    private static final String POSTGRESQL_IMAGE = "postgres:13.7-alpine";

    @Container
    final static PostgreSQLContainer<?> POSTGRES = createDatabaseInstance();


    @Autowired
    DroneRepository droneRepository;

    @Autowired
    OperationLogRepository operationLogRepository;

    @Test
    void droneSerialNumber_shouldBeUnique() {
        StepVerifier.create(droneRepository.save(new DroneEntity(null,
                                SAME_SERIAL_NUMBER,
                                DroneModelType.LIGHTWEIGHT,
                                DroneState.IDLE,
                                10,
                                20,
                                30))
                        .flatMap(entity -> droneRepository.save(
                                new DroneEntity(null,
                                        SAME_SERIAL_NUMBER,
                                        DroneModelType.HEAVYWEIGHT,
                                        DroneState.LOADING,
                                        40,
                                        50,
                                        60))))
                .expectError(DuplicateKeyException.class)
                .verify();
    }

    @Test
    void idempotencyKey_shouldBeUnique() {
        StepVerifier.create(operationLogRepository.save(
                new IdempotentOperationEntity(null, SAME_IDEMPOTENCY_KEY, 0, now()))
                        .flatMap(entity -> operationLogRepository.save(
                                new IdempotentOperationEntity(null, SAME_IDEMPOTENCY_KEY, 200, now()))))
                .expectError(DuplicateKeyException.class)
                .verify();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        // region Flyway
        registry.add("spring.flyway.url", PreconditionsTest::getPostgresqlUrlForFlyway);
        registry.add("spring.flyway.user", POSTGRES::getUsername);
        registry.add("spring.flyway.password", POSTGRES::getPassword);
        registry.add("spring.flyway.driver-class-name", POSTGRES::getDriverClassName);
        // endregion


        // region R2DBC
        registry.add("spring.r2dbc.url", PreconditionsTest::getPostgresqlUrlForR2dbc);
        registry.add("spring.r2dbc.username", POSTGRES::getUsername);
        registry.add("spring.r2dbc.password", POSTGRES::getPassword);
        // endregion
    }

    private static PostgreSQLContainer<?> createDatabaseInstance() {
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