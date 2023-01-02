package com.musalasoft.dronefleet;

import com.musalasoft.dronefleet.boundary.DroneMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication(proxyBeanMethods = false)
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Bean
	DroneMapper droneMapper() {
		return Mappers.getMapper(DroneMapper.class);
	}

}
