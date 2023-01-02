package com.musalasoft.dronefleet;

import com.musalasoft.dronefleet.boundary.DroneMapper;
import com.musalasoft.dronefleet.boundary.MedicationMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.musalasoft.dronefleet.r2dbc.DriverHostnameResolveWorkaround.resolveDatabaseHostName;


@SpringBootApplication(proxyBeanMethods = false)
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Bean
	DroneMapper droneMapper() {
		return Mappers.getMapper(DroneMapper.class);
	}

	@Bean
	MedicationMapper medicationMapper() {
		return Mappers.getMapper(MedicationMapper.class);
	}

	static {
		resolveDatabaseHostName();
	}
}
