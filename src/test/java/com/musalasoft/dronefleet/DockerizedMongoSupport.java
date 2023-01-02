package com.musalasoft.dronefleet;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class DockerizedMongoSupport {
    private static final String MONGODB_IMAGE = "mongo:5.0.14";

    @Container
    private static MongoDBContainer mongoDBContainer = new MongoDBContainer(MONGODB_IMAGE)
            .withReuse(true);


    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }


}
