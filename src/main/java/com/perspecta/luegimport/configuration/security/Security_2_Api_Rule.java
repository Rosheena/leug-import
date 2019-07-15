package com.perspecta.luegimport.configuration.security;

import com.perspecta.luegimport.business.service.security.LoginUserService;
import com.perspecta.luegimport.business.service.security.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Order(2)
public class Security_2_Api_Rule extends WebSecurityConfigurerAdapter {
	private final LoginUserService loginUserService;

	@Autowired
	public Security_2_Api_Rule(LoginUserService loginUserService) {
		this.loginUserService = loginUserService;
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.csrf().disable();

		http
				.antMatcher("/api/**")
				.authorizeRequests()
				.anyRequest().hasAnyRole(Roles.API)
				.and()
				.httpBasic().realmName("api");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(unSaltedPasswordAuthProvider());
	}

	private DaoAuthenticationProvider unSaltedPasswordAuthProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(loginUserService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	private PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
