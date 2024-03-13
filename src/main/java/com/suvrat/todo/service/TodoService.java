package com.suvrat.todo.service;

import com.suvrat.todo.entity.Todo;
import com.suvrat.todo.exception.TodoNotFoundException;
import com.suvrat.todo.pojo.ToDoResponseList;
import com.suvrat.todo.pojo.TodoRequest;
import com.suvrat.todo.pojo.TodoResponse;
import com.suvrat.todo.repository.TodoRepository;
import com.suvrat.todo.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoResponse createTodo(TodoRequest todoRequest) {
        var newTodo = new Todo(null, todoRequest.title(), todoRequest.completed());
        var todo = todoRepository.save(newTodo);
        return CommonUtils.todoEntityToResponse(todo);
    }

    public ToDoResponseList getAll() {
        var todos = todoRepository.findAll();
        return new ToDoResponseList(CommonUtils.todoEntityToResponse(todos));
    }

    public void delete() {
        todoRepository.deleteAll();
    }

    public TodoResponse findById(long id) {
        var todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("Todo does not exist with id " + id));
        return CommonUtils.todoEntityToResponse(todo);
    }

    public ToDoResponseList findByTitle(String title) {
        var todos = todoRepository.findByTitleContaining(title);
        return new ToDoResponseList(CommonUtils.todoEntityToResponse(todos));
    }

    public TodoResponse updateTodo(TodoRequest updates, long id) {
        var byId = todoRepository.findById(id);
        if (byId.isPresent()) {
            var existing = byId.get();
            var updatedTodo = new Todo(existing.getId(), updates.title(), updates.completed());
            return CommonUtils.todoEntityToResponse(todoRepository.save(updatedTodo));
        } else {
            throw new TodoNotFoundException("Todo does not exist with id " + id);
        }
    }

    public void deleteById(long id) {
        var byId = todoRepository.findById(id);
        if(byId.isEmpty()) throw new TodoNotFoundException("Todo does not exist with id " + id);
        todoRepository.deleteById(id);
    }

    public ToDoResponseList qb(String title) {
        var todos = todoRepository.searchByTitleContaining(title);
        return new ToDoResponseList(CommonUtils.todoEntityToResponse(todos));
    }

}
