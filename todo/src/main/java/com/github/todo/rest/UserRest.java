package com.github.todo.rest;

import java.time.LocalDateTime;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.github.todo.entity.User;
import com.github.todo.service.SecurityService;
import com.github.todo.service.TodoService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Path("user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserRest {

	@Inject
	private TodoService todoService;

	@Inject
	private SecurityService securityService;

	@Context
	private UriInfo uriInfo;

	@Path("create")
	@POST
	public Response create(@NotNull User user) {
		todoService.saveUser(user);
		return Response.ok(user).build();
	}

	@Path("login")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response login(@NotNull @FormParam("email") String email, @NotNull @FormParam("password") String password) {

		boolean authenticated = securityService.authenticateUser(email, password);

		if (!authenticated) {
			throw new SecurityException("Email or password not valid");
		}

		String token = generateToken(email);

		return Response.ok().header(HttpHeaders.AUTHORIZATION, SecurityService.BEARER + token).build();
	}

	private String generateToken(String email) {
		SecretKey securityKey = securityService.getSecurityKey();
		return Jwts.builder().setSubject(email).setIssuedAt(new Date()).setIssuer(uriInfo.getBaseUri().toString())
				.setAudience(uriInfo.getAbsolutePath().toString())
				.setExpiration(securityService.toDate(LocalDateTime.now().plusMinutes(60)))
				.signWith(SignatureAlgorithm.HS512, securityKey).compact();
	}

}
