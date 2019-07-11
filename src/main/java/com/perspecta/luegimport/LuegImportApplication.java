package com.perspecta.leugimport;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
public class LuegImportApplication {

	@Bean
	@Primary
	public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
		registration.addUrlMappings("/*", "/api/*", "/app/*");
		return registration;
	}


	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.serializationInclusion(JsonInclude.Include.NON_NULL);
		builder.failOnEmptyBeans(false);
		return builder;
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder()
				.sources(LuegImportApplication.class)
				.run(args);
	}

}
