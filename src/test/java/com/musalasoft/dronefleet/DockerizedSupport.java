package com.musalasoft.dronefleet;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static java.lang.Boolean.TRUE;

@Testcontainers
public abstract class DockerizedSupport {
    //private static final String MONGODB_IMAGE = "mongo:5.0.14";
    private static final String POSTGRESQL_IMAGE = "postgres:13.7-alpine";

    @Container
    final static PostgreSQLContainer<?> POSTGRES = createDatabaseInstance();


//    @Container
//    private static MongoDBContainer mongoDBContainer = new MongoDBContainer(MONGODB_IMAGE)
//            .withReuse(true);


    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        // region Flyway
        registry.add("spring.flyway.url", POSTGRES::getJdbcUrl);
        registry.add("spring.flyway.user", POSTGRES::getUsername);
        registry.add("spring.flyway.password", POSTGRES::getPassword);
        registry.add("spring.flyway.driver-class-name", POSTGRES::getDriverClassName);
        // endregion


        // region R2DBC
        registry.add("spring.r2dbc.url", () -> POSTGRES.getJdbcUrl().replace("jdbc", "r2dbc:pool"));
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


}
