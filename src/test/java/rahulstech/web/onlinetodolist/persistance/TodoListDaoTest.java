package rahulstech.web.onlinetodolist.persistance;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import rahulstech.web.onlinetodolist.model.Todo;
import rahulstech.web.onlinetodolist.model.TodoList;

public class TodoListDaoTest {

    private TodoListDao dao;

    @Before
    public void setUp() {
        dao = TodoListDao.getTestInstance();
    }

    @After
    public void tearDown() {
        if (null != dao) {
            dao.close();
            dao = null;
        }
    }

    @Test
    public void createNewTodoList() {
        TodoList newTodoList = new TodoList();
        newTodoList.setTitle("New TodoList");
        newTodoList.setCreated("2021-05-14 17:30");
        TodoList created = dao.createNewTodoList(newTodoList);
        Assert.assertNotNull("create todo list fail", created);
        long countTodoLists = countTodoLists();
        Assert.assertEquals("no new todo list created", 4, countTodoLists);
    }
    
    @Test
    public void getAllTodoLists() {
        List<TodoList> todoLists = dao.getAllTodoLists();
        Assert.assertEquals("todoLists.size() != 3", 3, null == todoLists ? 0 : todoLists.size());
    }
    
    @Test
    public void updateTodoList_Existing() {
        TodoList todoList = new TodoList();
        todoList.setId("3");
        todoList.setTitle("Update Title Todo List 3");
        todoList.setCreated("2021-05-21 09:00");
        
        dao.updateTodoList(todoList);
    }
    
    @Test
    public void updateTodoList_NotExisting() {
        final TodoList todoList = new TodoList();
        todoList.setId("12547");
        todoList.setTitle("Update Title For Not Existing TodoList");
        todoList.setCreated("2021-05-21 09:00");
        
        Assert.assertThrows("update not existing todolist does not throws EntityNotFoundException", 
                EntityNotFoundException.class,
                new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                dao.updateTodoList(todoList);
            }
        });
    }
    
    @Test
    public void deleteTodoList_Existing() {
        final String id = "2";
        dao.deleteTodoList(id);
    }
    
    @Test
    public void deleteTodoList_NotExisting() {
        final String id = "1025487";
        Assert.assertThrows("delete not existing todo list does not throws EntityNotFoundException",
                EntityNotFoundException.class,
                new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                dao.deleteTodoList(id);
            }
        });
    }
    
    @Test
    public void createNewTodo_ExistingTodoList() {
        Todo newTodo = new Todo();
        newTodo.setContent("Content for new todo");
        newTodo.setComplete(true);
        newTodo.setListId("3");
        
        Todo created = dao.createNewTodo(newTodo);
        Assert.assertNotNull("", created);
        long countTodos = countTodosForTodoList(newTodo.getListId());
        Assert.assertEquals("no todo created", 1, countTodos);
    }
    
    @Test
    public void createNewTodo_NotExistsingTodoList() {
        final Todo newTodo = new Todo();
        newTodo.setContent("Content for new todo");
        newTodo.setComplete(true);
        newTodo.setListId("214589764");
        
        Assert.assertThrows(
                "create todo for a not existing todo list does not thorws ConstraintViolationException",
                ConstraintViolationException.class,
                new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                dao.createNewTodo(newTodo);
            }
        });
    }
    
    @Test
    public void getAllTodosForTodoList_ExistingTodoList() {
        String listId = "1";
        List<Todo> todos = dao.getTodosForTodoListInRange(listId,-1,-1);
        Assert.assertEquals("todos not fetched properly for todo list, must fetch 4 todos",
                4,null == todos ? 0 : todos.size());
    }
    
    @Test
    public void getAllTodosForTodoList_NotExistingTodoList() {
        final String listId = "14458978564569";
        Assert.assertThrows("get todos for not existing todo list does not throw ConstraintViolationException",
                ConstraintViolationException.class,
                new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                dao.getTodosForTodoListInRange(listId,-1,-1);
            }
        });
    }
    
    @Test
    public void getAllTodosForTodoListInRange_ExistingTodoList() {
        String listId = "1";
        int start = 1;
        int end = 2;
        List<Todo> todos = dao.getTodosForTodoListInRange(listId,start,end);
        Assert.assertEquals("in range todos not fetched properly for todo list, must fetch 2 todos",
                2,null == todos ? 0 : todos.size());
    }
    
    @Test
    public void getAllTodosForTodoListInRange_NotExistingTodoList() {
        final String listId = "14458978564569";
        final int start = 1;
        final int end = 3;
        Assert.assertThrows("get todos in range for not existing todo list does not throw ConstraintViolationException",
                ConstraintViolationException.class,
                new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                dao.getTodosForTodoListInRange(listId,start,end);
            }
        });
    }
    
    @Test
    public void updateTodo_Existsting() {
        Todo todo = new Todo();
        todo.setId("4");
        todo.setContent("Updated content for existing todo");
        todo.setComplete(true);
        todo.setListId("1");
        
        dao.updateTodo(todo);
    }
    
    @Test
    public void updateTodo_NotExisting() {
        final Todo todo = new Todo();
        todo.setId("4798754657987");
        todo.setContent("Updated content for not existing todo");
        todo.setComplete(true);
        todo.setListId("2");
        
        Assert.assertThrows("update todo not existing does not throw EntityNotFoundException",
                EntityNotFoundException.class,
                new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                dao.updateTodo(todo);
            }
        });
    }
    
    @Test
    public void changeTodoComplete_Existing() {
        final String id = "4";
        final boolean complete = true;
        dao.changeTodoComplete(id, complete);
    }
    
    @Test
    public void changeTodoComplete_NotExisting() {
        final String id = "789787";
        final boolean complete = false;
        
        Assert.assertThrows("change todo complete not existing does not throw EntityNotFoundException",
                EntityNotFoundException.class,
                new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                dao.changeTodoComplete(id, complete);
            }
        });
    }
    
    @Test
    public void deleteTodo_Existing() {
        String id = "4";
        
        long countTodosBefore = countTodos();
        dao.deleteTodo(id);
        long countTodosAfter = countTodos();
        Assert.assertEquals("no todo deleted",1,countTodosBefore-countTodosAfter);
        
    }
    
    @Test
    public void deleteTodo_NotExisting() {
        final String id = "4557787567567574527478";
        
        long countTodosBefore = countTodos();
        Assert.assertThrows("delete not existing does not throw EntityNotFoundException",
                EntityNotFoundException.class,
                new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                dao.deleteTodo(id);
            }
        });
        
        long countTodosAfter = countTodos();
        Assert.assertEquals("some todos deleted",0,countTodosBefore-countTodosAfter);
    }
    
    private long countTodoLists() {
        try (Session session = dao.getSessionFactory().openSession()) {
            TypedQuery<Long> query = session.createQuery("SELECT COUNT(l.id) FROM todolists l",Long.class);
            return query.getSingleResult();
        }
    }
    
    private long countTodosForTodoList(String listId) {
        try (Session session = dao.getSessionFactory().openSession()) {
            TypedQuery<Long> query = session.createQuery("SELECT COUNT(t.id) FROM todos t WHERE listId = :listId",Long.class);
            query.setParameter("listId", listId);
            return query.getSingleResult();
        }
    }
    
    private long countTodos() {
        try (Session session = dao.getSessionFactory().openSession()) {
            TypedQuery<Long> query = session.createQuery("SELECT COUNT(t.id) FROM todos t",Long.class);
            return query.getSingleResult();
        }
    }
}
