package com.suvrat.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(value = TodoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleException(TodoNotFoundException exception){
        Map<String, Object> resp = Map.of("status", "failed", "code", 404, "msg", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception exception){
        Map<String, Object> resp = Map.of("status", "failed", "code", 500, "msg", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
    }
}
