package com.perspecta.leugimport;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@SpringBootApplication
public class LeugImportApplication {

	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.serializationInclusion(JsonInclude.Include.NON_NULL);
		builder.failOnEmptyBeans(false);
		return builder;
	}

	public static void main(String[] args) {
		SpringApplication.run(LeugImportApplication.class, args);
	}

}
