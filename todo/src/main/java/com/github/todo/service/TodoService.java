package com.github.todo.service;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.github.todo.entity.Todo;
import com.github.todo.entity.User;
import com.github.todo.rest.AuthContext;

@Stateless
public class TodoService {

	@Inject
	EntityManager entityManager;

	@Inject
	private QueryService queryService;

	@Inject
	private SecurityService securityService;

	@Inject
	private AuthContext authContext;

	public User saveUser(User user) {

		int count = queryService.countUserByEmail(user.getEmail());

		if (user.getId() == 0 && count == 0) {
			Map<String, String> credMap = securityService.hashPassword(user.getPassword());
			user.setPassword(credMap.get(SecurityService.HASHED_PASSWORD_KEY));
			user.setSalt(credMap.get(SecurityService.SALT));

			entityManager.persist(user);
			credMap.clear();
		}

		return user;
	}

	public Todo create(Todo todo) {

		User user = queryService.findUserByEmail(authContext.getEmail());

		if (user != null) {
			todo.setOwner(user);
			entityManager.persist(todo);
		}

		return todo;
	}

	public Todo update(Todo todo) {
		entityManager.merge(todo);
		return todo;
	}

	public Todo findById(long id) {
		return queryService.findTodoById(id);
	}

	public List<Todo> findAll() {
		return queryService.findAllTodos();
	}

	public Todo markAsComplete(long id) {

		Todo todo = findById(id);
		todo.setCompleted(!todo.isCompleted());
		update(todo);

		return todo;
	}

}
