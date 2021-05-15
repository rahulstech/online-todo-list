package rahulstech.web.onlinetodolist;

import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.hibernate.exception.ConstraintViolationException;
import rahulstech.web.onlinetodolist.model.Todo;
import rahulstech.web.onlinetodolist.model.TodoList;
import rahulstech.web.onlinetodolist.persistance.TodoListDao;
import rahulstech.web.onlinetodolist.util.ApiRequestException;

@Path("/")
@Consumes({MediaType.APPLICATION_JSON})
@Produces(MediaType.APPLICATION_JSON)
public class TodoListApi {

    private Logger logger = LogManager.getLogManager().getLogger(TodoListApi.class.getSimpleName());

    private TodoListDao dao = TodoListDao.getInstance();

    @POST
    @Path("/todolist")
    public TodoList createNewTodoList(@Context HttpServletResponse response, TodoList newList) {
        try {
            TodoList created = dao.createNewTodoList(newList);
            response.setStatus(HttpServletResponse.SC_CREATED);
            return created;
        } catch (Exception ex) {
            // TODO: log error
            throw ApiRequestException.internalServerError();
        }
    }

    @GET
    @Path("/todolists")
    public List<TodoList> getAllTodoList(
            @Context HttpServletResponse response,
            @QueryParam("todos") boolean todos) {
        try {
            List<TodoList> todoLists = dao.getAllTodoLists(todos);
            response.setStatus(HttpServletResponse.SC_OK);
            return todoLists;
        } catch (Exception e) {
            // TODO: log exception
            throw ApiRequestException.internalServerError();
        }
    }

    @PUT
    @Path("/todolist/{id}")
    public Response updateTodoList(@PathParam("id") String id, TodoList todoList) {
        try {
            todoList.setId(id);
            dao.updateTodoList(todoList);
            return Response.ok().build();
        } catch (EntityNotFoundException e) {
            // TODO: log exception
            throw ApiRequestException.notFound("No todo list found for given Id");
        } catch (Exception e) {
            // TODO: log exception
            throw ApiRequestException.internalServerError();
        }
    }

    @DELETE
    @Path("/todolist/{id}")
    public Response deleteTodoList(@PathParam("id") String id) {
        try {
            dao.deleteTodoList(id);
            return Response.ok().build();
        } catch (EntityNotFoundException ex) {
            // TODO: log exception
            throw ApiRequestException.notFound("");
        } catch (Exception ex) {
            // TODO: log exception
            throw ApiRequestException.internalServerError();
        }
    }

    @POST
    @Path("/todolist/{listId}/todo")
    public Todo createNewTodo(
            @Context HttpServletResponse response,
            @PathParam("listId") String listId, Todo newTodo) {
        try {
            newTodo.setListId(listId);
            Todo created = dao.createNewTodo(newTodo);
            response.setStatus(HttpServletResponse.SC_CREATED);
            return created;
        } catch (ConstraintViolationException ex) {
            // TODO: log exception
            throw ApiRequestException.notFound("No todo list found for given listId");
        } catch (Exception e) {
            // TODO: log exception
            throw ApiRequestException.internalServerError();
        }
    }

    @GET
    @Path("/todolist/{listId}/todos")
    public List<Todo> getTodos(
            @Context HttpServletResponse response,
            @PathParam("listId") String listId,
            @DefaultValue("-1") @QueryParam("start") int start,
            @DefaultValue("-1") @QueryParam("end") int end) {
        if (start > 0 && start > end) {
            throw ApiRequestException.badRequest("start value must be a positive and must be less than end value");
        }
        try {
            List<Todo> todos = dao.getTodosForTodoListInRange(listId, start, end);
            response.setStatus(HttpServletResponse.SC_OK);
            return todos;
        } catch (ConstraintViolationException ex) {
            // TODO: log exception
            throw ApiRequestException.notFound("No todo list found for given listId");
        } catch (Exception ex) {
            // TODO: log exception
            throw ApiRequestException.internalServerError();
        }
    }

    @PUT
    @Path("/todo/{id}")
    public Response updateTodo(@PathParam("id") String id, Todo todo) {
        try {
            todo.setId(id);
            dao.updateTodo(todo);
            return Response.ok().build();
        } catch (EntityNotFoundException ex) {
            // TODO: log exception
            throw ApiRequestException.notFound("No todo found with given id");
        } catch (Exception ex) {
            // TODO: log exception
            throw ApiRequestException.internalServerError();
        }
    }
    
    @PUT
    @Path("/todo/{id}/complete")
    public Response changeTodoComplete(@PathParam("id") String id, Todo todo) {
        try {
            dao.changeTodoComplete(id,todo.isComplete());
            return Response.ok().build();
        } catch (EntityNotFoundException ex) {
            // TODO: log exception
            throw ApiRequestException.notFound("No todo found with given id");
        } catch (Exception ex) {
            // TODO: log exception
            throw ApiRequestException.internalServerError();
        }
    }

    @DELETE
    @Path("/todo/{id}")
    public Response deleteTodo(@PathParam("id") String id) {
        try {
            dao.deleteTodo(id);
            return Response.ok().build();
        } catch (EntityNotFoundException ex) {
            // TODO: log exception
            throw ApiRequestException.notFound("No todo found with given id");
        } catch (Exception ex) {
            // TODO: log exception
            throw ApiRequestException.internalServerError();
        }
    }
}
