package com.github.todo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user")
@NamedQuery(name = User.FIND_ALL_USERS, query = "SELECT u FROM User u ORDER BY u.fullName")
@NamedQuery(name = User.FIND_USER_BY_EMAIL, query = "SELECT u FROM User u WHERE u.email = :email")
public class User extends AbstractEntity {

	public static final String FIND_ALL_USERS = "User.findAllUsers";
	public static final String FIND_USER_BY_EMAIL = "User.findByEmail";

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Full name must be set")
	@Size(min = 2, message = "Name must contain at leat 2 characters")
	@Column(name = "full_name")
	private String fullName;

	@NotNull(message = "Email must be set")
	@Email(message = "Email must be in the form user@domain.com")
	private String email;

	@NotNull(message = "Password cannot be empty")
	@Size(min = 8, message = "Password must have a minimum size of 8 characters")
	private String password;

	private String salt;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

}
