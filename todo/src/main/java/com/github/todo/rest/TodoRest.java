package com.github.todo.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.github.todo.entity.Todo;
import com.github.todo.service.TodoService;

@Path("todo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authz
public class TodoRest {

	@Inject
	private TodoService todoService;

	@Path("create")
	@POST
	public Response create(Todo todo) {

		todoService.create(todo);

		return Response.ok(todo).build();
	}

	@Path("update")
	@PUT
	public Response update(Todo todo) {
		todoService.update(todo);
		return Response.ok(todo).build();
	}

	@Path("{id}")
	@GET
	public Todo getTodo(@PathParam("id") long id) {
		return todoService.findById(id);
	}

	@Path("list")
	@GET
	public List<Todo> getTodos() {
		return todoService.findAll();
	}

	@Path("status")
	@POST
	public Response markAsComplete(@QueryParam("id") long id) {
		return Response.ok(todoService.markAsComplete(id)).build();
	}

}
