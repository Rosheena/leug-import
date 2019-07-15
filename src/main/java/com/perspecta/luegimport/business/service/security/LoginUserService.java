package com.perspecta.luegimport.business.service.security;

import com.perspecta.luegimport.business.domain.user.User;
import com.perspecta.luegimport.business.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserService implements UserDetailsService {

	private final UserRepository userSecurityRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userSecurityRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("No such user");
		}

		return new LoginUser(user);
	}
}
