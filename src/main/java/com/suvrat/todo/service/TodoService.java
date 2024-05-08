package com.suvrat.todo.service;

import com.github.javafaker.Faker;
import com.google.common.collect.Lists;
import com.suvrat.todo.entity.Todo;
import com.suvrat.todo.entity.UserEntity;
import com.suvrat.todo.exception.TodoNotFoundException;
import com.suvrat.todo.pojo.ToDoResponseList;
import com.suvrat.todo.pojo.TodoRequest;
import com.suvrat.todo.pojo.TodoResponse;
import com.suvrat.todo.repository.TodoRepository;
import com.suvrat.todo.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

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

    @Async
    public CompletableFuture<Integer> asyncTest() throws InterruptedException {
        var sum = 0;
        for (int i = 0; i < 100; i++) {
            System.out.println("Inside async ============== " + (i+1));
            Thread.sleep(100);
            sum += i;
        }
        final int finalSum = sum;
        return CompletableFuture.supplyAsync(() -> finalSum);
    }

    @Async
    public CompletableFuture<List<Todo>> bulkInsert(){
        System.out.println("Inside async ================= ");
        var faker = Faker.instance();
        var toDoList = Stream.generate(
                        () -> new Todo(null,
                                faker.funnyName().name(),
                                faker.bool().bool(),
                                Set.of(new UserEntity(), new UserEntity())))
                .limit(1000).toList();
        System.out.println(toDoList);
        var partition = Lists.partition(toDoList, 100);
        final var list = partition.stream().map(todoRepository::saveAll).flatMap(List::stream).toList();
        return CompletableFuture.supplyAsync(() -> list);
    }

}
