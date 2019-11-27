package uniupo.gaborgalazzo.demospring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uniupo.gaborgalazzo.demospring.domain.Todo;
import uniupo.gaborgalazzo.demospring.repository.TodoRepository;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/todo")
public class TodoController {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @RequestMapping("/{id}")
    public Todo getTodo( @PathVariable int id){
        return todoRepository.findById(id);

    }

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public List<Todo> getAll(){
        return todoRepository.findAll();

    }

    @RequestMapping(value = "/", method = {RequestMethod.POST})
    public Todo saveTodo(@RequestBody Todo todo){
        return todoRepository.save(todo);

    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT})
    public Todo updateTodo(@RequestBody Todo todo,  @PathVariable int id){
        todo.setId(id);
        return todoRepository.update(todo);

    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    public boolean updateTodo(@PathVariable int id){
        return todoRepository.delete(id);

    }
}
