package com.github.todo.rest;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class AuthContext {

	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
