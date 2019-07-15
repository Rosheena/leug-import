package com.perspecta.luegimport.configuration.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.stream.Stream;

@Configuration
@Order(4)
public class Security_4_Ignore_Rule extends WebSecurityConfigurerAdapter {

	private static final String[] ACTUATOR_URLS = {"/health"};
	private static final String[] APP_URLS = {"/", "/index", "/login", "/app-index.html", "/app-resources/**"};

	@Override
	public void configure(WebSecurity web) throws Exception {
		web
				.ignoring()
				.antMatchers(Stream.of(APP_URLS, ACTUATOR_URLS).flatMap(Stream::of).toArray(String[]::new));
	}
}