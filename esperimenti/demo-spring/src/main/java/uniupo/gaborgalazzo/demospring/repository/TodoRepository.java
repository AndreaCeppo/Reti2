package uniupo.gaborgalazzo.demospring.repository;

import org.springframework.stereotype.Service;
import uniupo.gaborgalazzo.demospring.domain.Todo;

import java.util.ArrayList;
import java.util.List;

@Service
public class TodoRepository {

    private int id = 0;

    private ArrayList<Todo> todos;

    public TodoRepository() {
        this.todos = new ArrayList<>();
    }

    public Todo findById(int id){
        for(Todo todo: todos){
            if(todo.getId() == id)
                return todo;
        }
        return null;
    }

    public List<Todo> findAll(){
        return todos;
    }

    public Todo save(Todo todo){
        id++;
        todo.setId(id);
        todos.add(todo);
        return todo;
    }

    public Todo update(Todo todo){
        Todo old = findById(todo.getId());
        old.setDone(todo.isDone());
        old.setText(todo.getText());
        return old;
    }

    public boolean delete(int id){
       return todos.remove(findById(id));
    }
}
