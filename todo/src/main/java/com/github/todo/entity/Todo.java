package com.github.todo.entity;

import java.time.LocalDate;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "todo")
@NamedQuery(name = Todo.FIND_TODO_BY_ID, query = "SELECT t FROM Todo t WHERE t.id = :id AND t.owner.email = :email")
@NamedQuery(name = Todo.FIND_TODO_BY_TASK, query = "SELECT t FROM Todo t WHERE t.task LIKE :task AND t.owner.email = :email")
@NamedQuery(name = Todo.FIND_ALL_TODOS_BY_USER, query = "SELECT t FROM Todo t WHERE t.owner.email = :email")
public class Todo extends AbstractEntity {

	public static final String FIND_TODO_BY_ID = "Todo.findById";
	public static final String FIND_TODO_BY_TASK = "Todo.findByTask";
	public static final String FIND_ALL_TODOS_BY_USER = "Todo.findByUser";

	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "Task must be set")
	@Size(min = 3, message = "Task should not be less than 3 characters")
	private String task;

	@NotNull(message = "Due date must be set")
	@FutureOrPresent(message = "Due date must be in the present or future")
	@JsonbDateFormat(value = "yyyy-MM-dd")
	@Column(name = "due_date")
	private LocalDate dueDate;

	@Column(name = "is_completed")
	private boolean isCompleted;

	@JsonbDateFormat(value = "yyyy-MM-dd")
	@Column(name = "date_completed")

	private LocalDate dateCompleted;

	@JsonbDateFormat(value = "yyyy-MM-dd")
	@Column(name = "date_created")
	private LocalDate dateCreated;

	@ManyToOne
	private User owner;

	@PrePersist
	private void init() {
		this.dateCreated = LocalDate.now();
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public LocalDate getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(LocalDate dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

}
