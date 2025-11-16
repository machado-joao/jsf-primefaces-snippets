package com.github.todo.rest;

import java.io.IOException;
import java.security.Key;
import java.security.Principal;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import com.github.todo.service.SecurityService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@Authz
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {

	@Inject
	private SecurityService securityService;

	@Inject
	private AuthContext authContext;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		String authString = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		if (authString == null || authString.isEmpty() || !authString.startsWith("Bearer")) {
			throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED).build());
		}

		String token = authString.substring(SecurityService.BEARER.length()).trim();

		try {

			Key key = securityService.getSecurityKey();
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
			String email = claimsJws.getBody().getSubject();

			authContext.setEmail(email);
			SecurityContext originalContext = requestContext.getSecurityContext();

			requestContext.setSecurityContext(new SecurityContext() {

				@Override
				public Principal getUserPrincipal() {
					return new Principal() {
						@Override
						public String getName() {
							return email;
						}

					};
				}

				@Override
				public boolean isUserInRole(String role) {
					return originalContext.isUserInRole(role);
				}

				@Override
				public boolean isSecure() {
					return originalContext.isSecure();
				}

				@Override
				public String getAuthenticationScheme() {
					return originalContext.getAuthenticationScheme();
				}
			});

		} catch (Exception e) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}

	}

}
