package com.suvrat.todo.utils;

import com.suvrat.todo.entity.Todo;
import com.suvrat.todo.pojo.TodoResponse;

import java.util.List;

public class CommonUtils {
    public static List<TodoResponse> todoEntityToResponse(List<Todo> todos){
        return todos.parallelStream().map(CommonUtils::todoEntityToResponse).toList();
    }

    public static TodoResponse todoEntityToResponse(Todo todo){
        return new TodoResponse(todo.getId(), todo.getTitle(), todo.isCompleted(), todo.getUserEntities());
    }
}
