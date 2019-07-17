package com.perspecta.luegimport.business.domain.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "User")
public class User implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "userId")
	private Long id;

	private String username;

	private String password;

	private String role;
}
