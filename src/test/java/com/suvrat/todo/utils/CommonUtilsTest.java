package com.suvrat.todo.utils;

import com.suvrat.todo.entity.Todo;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommonUtilsTest {

    @Test
    public void todoEntityToResponse(){
        var entity = getEntity();
        var todoResponse = CommonUtils.todoEntityToResponse(entity);
        assertEquals(entity.getId(), todoResponse.id());
        assertEquals(entity.getTitle(), todoResponse.title());
        assertEquals(entity.isCompleted(), todoResponse.completed());
    }

    @Test
    public void todoEntityToResponseList(){
        var entity = getEntity();
        var todoResponse = CommonUtils.todoEntityToResponse(List.of(entity));
        assertFalse(todoResponse.isEmpty());
        assertEquals(1, todoResponse.size());
    }

    private Todo getEntity(){
        return new Todo(1L, "test1", false);
    }
}
