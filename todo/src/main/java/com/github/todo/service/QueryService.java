package com.github.todo.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.github.todo.entity.Todo;
import com.github.todo.entity.User;
import com.github.todo.rest.AuthContext;

@Stateless
public class QueryService {

	@Inject
	EntityManager entityManager;

	@Inject
	private SecurityService securityService;

	@Inject
	private AuthContext authContext;

	public User findUserByEmail(String email) {

		List<User> resultList = entityManager.createNamedQuery(User.FIND_USER_BY_EMAIL, User.class)
				.setParameter("email", email).getResultList();

		if (!resultList.isEmpty()) {
			return resultList.get(0);
		}

		return null;
	}

	public int countUserByEmail(String email) {

		Long count = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
				.setParameter("email", email).getSingleResult();

		return count.intValue();
	}

	public boolean authenticateUser(String email, String password) {

		User user = findUserByEmail(email);

		if (user == null) {
			return false;
		}

		return securityService.passwordsMatch(user.getPassword(), user.getSalt(), password);
	}

	public Todo findTodoById(long id) {

		List<Todo> resultList = entityManager.createNamedQuery(Todo.FIND_TODO_BY_ID, Todo.class).setParameter("id", id)
				.setParameter("email", authContext.getEmail()).getResultList();

		if (!resultList.isEmpty()) {
			return resultList.get(0);
		}

		return null;
	}

	public List<Todo> findAllTodos() {
		return entityManager.createNamedQuery(Todo.FIND_ALL_TODOS_BY_USER, Todo.class)
				.setParameter("email", authContext.getEmail()).getResultList();
	}

	public List<Todo> findAllTodosByTask(String taskText) {
		return entityManager.createNamedQuery(Todo.FIND_TODO_BY_TASK, Todo.class)
				.setParameter("email", authContext.getEmail()).setParameter("task", "%" + taskText + "%")
				.getResultList();
	}

}
