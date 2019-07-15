package com.perspecta.luegimport.business.service.security;

import com.perspecta.luegimport.business.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LoginUser implements UserDetails {

	private final String username;
	private final String password;
	private final User user;
	private final List<SimpleGrantedAuthority> authorities = new ArrayList<>();
	private boolean enabled;

	LoginUser(User user) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.user = user;
		this.authorities.add(new SimpleGrantedAuthority(user.getRole()));
		this.enabled = true;
	}

	public User getUser() {
		return user;
	}

	@Override
	public List<SimpleGrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
