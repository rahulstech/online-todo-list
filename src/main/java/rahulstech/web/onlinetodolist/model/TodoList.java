package rahulstech.web.onlinetodolist.model;

import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name="todolists")
@Table(name="todolists")
public class TodoList {
   
    @Id
    @Column(insertable=true,updatable=false)
    private String id;
    
    @Column(nullable=false)
    private String created;
    
    @Column(nullable=false)
    private String title;
    
    @OneToMany(fetch=FetchType.EAGER)
    private List<Todo> todos = null;
   
    public TodoList() {}
    
    public TodoList(String id, String title, String created) {
        this.id = id;
        this.title = title;
        this.created = created;
        this.todos = null;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getCreated() {
        return created;
    }
    
    public void setCreated(String created) {
        this.created = created;
    }
    
    public List<Todo> getTodos() {
        return todos;
    }
    
    public void setTodos(List<Todo> todos) {
        this.todos = todos;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof TodoList) {
            TodoList l = (TodoList) o;
            return Objects.equals(l.id, this.id);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "TodoList{id=\""+id+"\",\n"
                + "title=\""+title+"\",\n"
                + "created=\""+created+"\",\n"+
                "todos="+(null == todos ? "[]" : todos)+"}";
    }
}
