package com.suvrat.todo.controller;

import com.suvrat.todo.config.DataSourceConfigs;
import com.suvrat.todo.pojo.ToDoResponseList;
import com.suvrat.todo.pojo.TodoRequest;
import com.suvrat.todo.pojo.TodoResponse;
import com.suvrat.todo.service.TodoService;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/todos")
@RequiredArgsConstructor
@Slf4j
public class TodoController {
    private final TodoService todoService;
    private final HikariDataSource dataSource;
    private final DataSourceConfigs dataSourceConfigs;
    private final String CLASS_NAME = this.getClass().getSimpleName();

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@RequestBody TodoRequest todoRequest) {
        log.info("Execution started -> {} -> createTodo", CLASS_NAME);
        var todo = todoService.createTodo(todoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(todo);
    }

    @GetMapping
    public ResponseEntity<ToDoResponseList> getAll() {
        log.info("Execution started -> {} -> getAll", CLASS_NAME);
        var todos = todoService.getAll();
        return ResponseEntity.ok(todos);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> delete() {
        log.info("Execution started -> {} -> delete", CLASS_NAME);
        todoService.delete();
        return ResponseEntity.ok(Map.of("msg", "deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> findById(@PathVariable long id) {
        log.info("Execution started -> {} -> findById", CLASS_NAME);
        var todo = todoService.findById(id);
        return ResponseEntity.ok(todo);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<ToDoResponseList> findByTitle(@PathVariable String title) {
        log.info("Execution started -> {} -> findByTitle", CLASS_NAME);
        var todo = todoService.findByTitle(title);
        return ResponseEntity.ok(todo);
    }

    @GetMapping("/title2/{title}")
    public ResponseEntity<ToDoResponseList> qb(@PathVariable String title) {
        log.info("Execution started -> {} -> findByTitle", CLASS_NAME);
        System.out.println(Thread.currentThread().threadId());
        System.out.println(this);
        var todo = todoService.qb(title);
        return ResponseEntity.ok(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTodo(@RequestBody TodoRequest updates, @PathVariable long id) {
        log.info("Execution started -> {} -> updateTodo", CLASS_NAME);
        var response = todoService.updateTodo(updates, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteById(@PathVariable long id) {
        log.info("Execution started -> {} -> deleteById", CLASS_NAME);
        todoService.deleteById(id);
        return ResponseEntity.ok(Map.of("msg", String.format("deleted successfully with id %s", id)));
    }

    @PostConstruct
    public void test(){
        System.out.println("Datasource ============= ");
        dataSource.setMaximumPoolSize(100);
        int maximumPoolSize = dataSource.getMaximumPoolSize();
        System.out.println(dataSource.getClass().getName() + "    " + maximumPoolSize);
        System.out.println("Datasource ============= ");
        System.out.println(dataSourceConfigs);
    }
}
