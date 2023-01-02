package com.musalasoft.dronefleet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class AppTests extends DockerizedTestSupport {

	@Test
	void contextLoads() {
	}

}
