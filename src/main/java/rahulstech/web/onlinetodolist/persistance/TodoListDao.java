package rahulstech.web.onlinetodolist.persistance;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import rahulstech.web.onlinetodolist.model.Todo;
import rahulstech.web.onlinetodolist.model.TodoList;

public class TodoListDao {

    private static TodoListDao mInstance;

    private SessionFactory sf = null;

    SessionFactory getSessionFactory() {
        return sf;
    }

    private TodoListDao(boolean createTestInstance) {
        initialize(createTestInstance);
    }

    private void initialize(boolean initializeForTest) {
        String conf_file_name = initializeForTest ? "hibernate.test.cfg.xml" : "hibernate.cfg.xml";
        Configuration conf = new Configuration()
                .configure(conf_file_name);
        this.sf = conf.buildSessionFactory();
    }

    public static TodoListDao getInstance() {
        if (mInstance == null) {
            mInstance = new TodoListDao(false);
        }
        return mInstance;
    }

    static TodoListDao getTestInstance() {
        if (mInstance == null) {
            mInstance = new TodoListDao(true);
        }
        return mInstance;
    }

    public void close() {
        try {
            sf.close();
        } catch (Exception ignored) {
        } finally {
            sf = null;
            mInstance = null;
        }
    }

    public TodoList createNewTodoList(TodoList newList) throws EntityExistsException {
        try ( Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            newList.setId(UUID.randomUUID().toString());
            session.save(newList);
            transaction.commit();
        }
        return newList;
    }

    public List<TodoList> getAllTodoLists(boolean fetchAssociatedTodos) {
        try ( Session session = sf.openSession()) {
            TypedQuery<TodoList> query = session.createQuery("FROM todolists", TodoList.class);
            List<TodoList> todoLists = query.getResultList();
            return todoLists;
        }
    }

    public List<TodoList> getAllTodoLists() {
        try ( Session session = sf.openSession()) {
            TypedQuery<TodoList> query = session.createQuery("SELECT new todolists(l.id,l.title,l.created) "
                    + "FROM todolists l", TodoList.class);
            List<TodoList> todoLists = query.getResultList();
            return todoLists;
        }
    }

    public void updateTodoList(TodoList todoList) throws EntityNotFoundException {
        getByIdOnExistsOrThrow(TodoList.class, todoList.getId());
        try ( Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(todoList);
            transaction.commit();
        }
    }

    public void deleteTodoList(String id) throws EntityNotFoundException {
        TodoList todoList = getByIdOnExistsOrThrow(TodoList.class, id);
        try ( Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(todoList);
            transaction.commit();
        }
    }

    public Todo createNewTodo(Todo newTodo) throws ConstraintViolationException {
        checkTodoListConstraintOrThrow(newTodo.getListId());
        try ( Session session = sf.openSession()) {
            newTodo.setId(UUID.randomUUID().toString());
            Transaction transaction = session.beginTransaction();
            session.save(newTodo);
            transaction.commit();
            return newTodo;
        }
    }

    public List<Todo> getTodosForTodoListInRange(String listId,
            int start, int end) throws ConstraintViolationException {
        checkTodoListConstraintOrThrow(listId);
        try ( Session session = sf.openSession()) {
            TypedQuery<Todo> query = session.createQuery("SELECT new todos(t.id,t.content,t.complete,t.todoList.id) "
                    + "FROM todos t WHERE listId = :listId", Todo.class);
            query.setParameter("listId", listId);
            if (start > 0 && end > start) {
                int first = start-1;
                int max = end - start + 1;
                query.setFirstResult(first)
                        .setMaxResults(max);
            }
            return query.getResultList();
        }
    }

    public void updateTodo(Todo todo) throws EntityNotFoundException {
        getByIdOnExistsOrThrow(Todo.class, todo.getId());
        _updateTodo(todo);
    }

    public void changeTodoComplete(String id, boolean complete) throws EntityNotFoundException {
        Todo todo = getByIdOnExistsOrThrow(Todo.class, id);
        if (todo.isComplete() == complete) return;
        todo.setComplete(complete);
        _updateTodo(todo);
    }

    public void deleteTodo(String id) throws EntityNotFoundException {
        Todo todo = getByIdOnExistsOrThrow(Todo.class, id);
        try ( Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(todo);
            transaction.commit();
        }
    }

    private void checkTodoListConstraintOrThrow(String listId) throws ConstraintViolationException {
        try {
            getByIdOnExistsOrThrow(TodoList.class, listId);
        } catch (EntityNotFoundException ex) {
            throw new ConstraintViolationException("no todo list found", null, null);
        }
    }

    private void _updateTodo(Todo todo) {
        try ( Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(todo);
            transaction.commit();
        }
    }

    private <T> T getByIdOnExistsOrThrow(Class<T> entityClass, Object pk) throws EntityNotFoundException {
        T row = null;
        try ( Session session = sf.openSession()) {
            row = session.get(entityClass, (Serializable) pk);
        } finally {
            if (null == row) {
                throw new EntityNotFoundException("not record found of type " + entityClass.getName() + " with pk=" + pk);
            }
        }
        return row;
    }

}
