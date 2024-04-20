package com.suvrat.todo;

import com.suvrat.todo.pojo.ToDoResponseList;
import com.suvrat.todo.service.TodoService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class TodoExampleApplication {

	private final TodoService todoService;

	public static void main(String[] args) {
		SpringApplication.run(TodoExampleApplication.class, args);
	}

	@PostConstruct
	public void test() throws InterruptedException {
		CompletableFuture<Integer> cpInt = todoService.asyncTest();
		cpInt
				.thenAccept(this::print)
				.exceptionally(this::handelException)
				.thenRun(() -> System.out.println("Completed"));
		ToDoResponseList all = todoService.getAll();
		System.out.println(all);
	}

	private void print(int v) {
		System.out.println("Returned value === " + v);
	}

	private Void handelException(Throwable th) {
		log.error("Exception occurred {}", ExceptionUtils.getRootCause(th).toString());
		System.out.println("Returned value === " + th.getMessage());
		return null;
	}

}
