package com.suvrat.todo.repository;

import com.suvrat.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByTitleContaining(String title);

    List<Todo> searchByTitleContaining(String title);
}
