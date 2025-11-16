package com.github.todo.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.util.ByteSource;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

@ApplicationScoped
public class SecurityService {

	@Inject
	private QueryService queryService;

	public static final String HASHED_PASSWORD_KEY = "hashedPassword";
	public static final String SALT = "salt";
	public static final String BEARER = "Bearer ";
	private SecretKey securityKey;

	@PostConstruct
	private void init() {
		this.securityKey = generateKey();
	}

	public Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public boolean passwordsMatch(String dbStoredHashedPassword, String saltText, String clearTextPassword) {

		ByteSource salt = ByteSource.Util.bytes(Hex.decode(saltText));
		String hashedPassword = hashAndSaltPassword(clearTextPassword, salt);

		return hashedPassword.equals(dbStoredHashedPassword);
	}

	public Map<String, String> hashPassword(String clearTextPassword) {

		ByteSource salt = getSalt();
		Map<String, String> credMap = new HashMap<>();
		credMap.put(HASHED_PASSWORD_KEY, hashAndSaltPassword(clearTextPassword, salt));
		credMap.put(SALT, salt.toHex());

		return credMap;
	}

	private String hashAndSaltPassword(String clearTextPassword, ByteSource salt) {
		return new Sha512Hash(clearTextPassword, salt, 2000000).toHex();
	}

	private ByteSource getSalt() {
		return new SecureRandomNumberGenerator().nextBytes();
	}

	public boolean authenticateUser(String email, String password) {
		return queryService.authenticateUser(email, password);
	}

	private SecretKey generateKey() {
		return MacProvider.generateKey(SignatureAlgorithm.HS512);
	}

	public SecretKey getSecurityKey() {
		return securityKey;
	}

}
