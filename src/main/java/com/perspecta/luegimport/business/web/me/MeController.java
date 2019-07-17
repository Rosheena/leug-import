package com.perspecta.luegimport.business.web.me;

import com.perspecta.luegimport.business.service.security.CurrentUser;
import com.perspecta.luegimport.business.web.me.object.Me;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
public class MeController {

	@PreAuthorize("hasAnyRole(@Roles.ROLE_USER)")
	@GetMapping
	public Me me() {
		return Me.init(CurrentUser.getUser());
	}
}
