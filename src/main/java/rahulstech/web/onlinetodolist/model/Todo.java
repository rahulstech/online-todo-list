package rahulstech.web.onlinetodolist.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity(name="todos")
@Table(name="todos")
public class Todo {
    
    @Id
    @Column(insertable=true,updatable=false)
    private String id;
    
    @Column(nullable=false)
    private String content;
    
    @Column(nullable=false)
    @ColumnDefault("false")
    private boolean complete;
    
    @ManyToOne
    @JoinColumn(name="listId",
            referencedColumnName="id",
            nullable=false,updatable=false)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private TodoList todoList;
    
    public Todo() {}
    
    public Todo(String id, String content, boolean complete, String listId) {
        this.id = id;
        this.content = content;
        this.complete = complete;
        setListId(listId);
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public boolean isComplete() {
        return complete;
    }
    
    public void setComplete(boolean complete) {
        this.complete = complete;
    }
    
    
    public TodoList getTodoList() {
        return todoList;
    }
    
    public void setTodoList(TodoList todoList) {
        this.todoList = todoList;
    }
    
    public String getListId() {
        return todoList.getId();
    }
    
    public void setListId(String listId) {
        TodoList todoList = new TodoList();
        todoList.setId(listId);
        setTodoList(todoList);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Todo) {
            Todo t = (Todo) o;
            return Objects.equals(t.id,this.id);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Todo {id=\""+id+"\"\n" +
                ",content=\""+content+"\",\n" +
                "complete="+complete+",\n" +
                "listId=\""+(null == todoList ? "" : todoList.getId())+"\"}";
    }
}
