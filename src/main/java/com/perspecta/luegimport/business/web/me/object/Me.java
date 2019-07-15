package com.perspecta.luegimport.business.web.me.object;

import com.perspecta.luegimport.business.domain.user.User;

public class Me {
	private final String username;
	private final String authority;

	public static Me init(User user) {
		return new Me(user);
	}

	public String getUsername() {
		return username;
	}

	public String getAuthority() {
		return authority;
	}

	private Me(User user) {
		this.username = user.getUsername();
		this.authority = user.getRole();
	}
}
