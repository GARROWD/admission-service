package ru.garrowd.admissionservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Conditions;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition
@EnableCaching
@Slf4j
public class AdmissionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdmissionServiceApplication.class, args);
	}
}
