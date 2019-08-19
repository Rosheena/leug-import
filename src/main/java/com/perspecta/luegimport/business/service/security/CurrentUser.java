package com.perspecta.luegimport.business.service.security;

import com.perspecta.luegimport.business.common.delegate.ErrorDelegate;
import com.perspecta.luegimport.business.domain.user.User;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.charset.Charset;
import java.util.Optional;

public final class CurrentUser {

	public static User getUser() {
		return Optional.ofNullable(getLoginUser())
				.map(LoginUser::getUser)
				.orElse(null);
	}

	public static SecurityExpressionOperations securityOperations() {
		return Optional.ofNullable(getAuthentication())
				.map(SecurityOperations::new)
				.orElse(null);
	}

	public static String getBasicAuthorization() {
		LoginUser loginUser = Optional.ofNullable(getLoginUser()).orElseThrow(() -> ErrorDelegate.returnBadRequest("Cannot retrieve current user credentials"));

		return "Basic " + new String(Base64.encodeBase64(String.format("%s:%s", loginUser.getUsername(), loginUser.getPassword()).getBytes(Charset.forName("US-ASCII"))));
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

	private static class SecurityOperations extends SecurityExpressionRoot {

		private SecurityOperations(Authentication authentication) {
			super(authentication);
		}
	}
}
