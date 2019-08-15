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
@Order(3)
public class Security_3_App_Rule extends WebSecurityConfigurerAdapter {
	private final LoginUserService loginUserService;

	@Autowired
	public Security_3_App_Rule(LoginUserService loginUserService) {
		this.loginUserService = loginUserService;
	}

	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.csrf().disable();

		http
				.antMatcher("/app/**")
				.authorizeRequests()
				.anyRequest().hasAnyRole(Roles.USER)
				.and()
				.httpBasic().realmName("app");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(unSaltedPasswordAuthProvider());
	}

	private DaoAuthenticationProvider unSaltedPasswordAuthProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(loginUserService);
		authProvider.setPasswordEncoder(passwordEncoder());
		authProvider.setHideUserNotFoundExceptions(false);

		return authProvider;
	}

	private PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
