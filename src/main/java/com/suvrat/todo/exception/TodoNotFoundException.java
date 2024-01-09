package com.suvrat.todo.exception;

public class TodoNotFoundException extends RuntimeException{
    public TodoNotFoundException(String msg){
        super(msg);
    }
}
