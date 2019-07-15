package com.perspecta.luegimport.business.service.security;

import com.perspecta.luegimport.business.domain.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class CurrentUser {

	public static User getUser() {
		return Optional.ofNullable(getLoginUser())
				.map(LoginUser::getUser)
				.orElse(null);
	}

	private static LoginUser getLoginUser() {
		return Optional.ofNullable(getAuthentication())
				.map(Authentication::getPrincipal)
				.filter(LoginUser.class::isInstance)
				.map(LoginUser.class::cast)
				.orElse(null);
	}

	private static Authentication getAuthentication() {
		return Optional.ofNullable(SecurityContextHolder.getContext())
				.map(SecurityContext::getAuthentication)
				.orElse(null);
	}
}
