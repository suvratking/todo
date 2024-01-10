package com.suvrat.todo.service;

import com.suvrat.todo.entity.Todo;
import com.suvrat.todo.exception.TodoNotFoundException;
import com.suvrat.todo.pojo.TodoRequest;
import com.suvrat.todo.repository.TodoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockServiceTest {
    @Mock
    private TodoRepository todoRepository;
    @InjectMocks
    private TodoService todoService;

    @Test
    public void createTodo_test(){
        var req = getReq();
        var entity = getEntity();
        when(todoRepository.save(any(Todo.class))).thenReturn(entity);
        var todo = todoService.createTodo(req);
        assertEquals(entity.getTitle(), todo.title());
        assertEquals(entity.isCompleted(), todo.completed());
    }

    @Test
    public void getAll_test(){
        var entity = getEntity();
        when(todoRepository.findAll()).thenReturn(List.of(entity));
        var findAll = todoService.getAll();
        assertFalse(findAll.todos().isEmpty());
        assertEquals(1, findAll.todos().size());
    }

    @Test
    public void deleteAll_test(){
        doNothing().when(todoRepository).deleteAll();
        todoService.delete();
        verify(todoRepository, times(1)).deleteAll();
    }

    @Test
    public void findById_test(){
        var entity = getEntity();
        when(todoRepository.findById(1L)).thenReturn(Optional.of(entity));
        var byId = todoService.findById(1L);
        assertEquals(entity.getId(), byId.id());
        assertEquals(entity.getTitle(), byId.title());
        assertEquals(entity.isCompleted(), byId.completed());
    }

    @Test(expected = TodoNotFoundException.class)
    public void findById_test_invalid(){
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());
        todoService.findById(1L);
    }

    @Test
    public void findByTitle_test(){
        var entity = getEntity();
        when(todoRepository.findByTitleContaining("test")).thenReturn(List.of(entity));
        var test = todoService.findByTitle("test");
        assertFalse(test.todos().isEmpty());
        assertEquals(1, test.todos().size());
    }

    @Test
    public void update_test(){
        var entity = getEntity();
        var req = new TodoRequest("test1", true);
        when(todoRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        entity.setCompleted(true);
        when(todoRepository.save(any(Todo.class))).thenReturn(entity);
        var todoResponse = todoService.updateTodo(req, entity.getId());
        assertEquals(entity.getId(), todoResponse.id());
        assertEquals(entity.getTitle(), todoResponse.title());
        assertSame(true, todoResponse.completed());
    }

    @Test(expected = TodoNotFoundException.class)
    public void update_test_invalid(){
        var req = new TodoRequest("test1", true);
        when(todoRepository.findById(10L)).thenReturn(Optional.empty());
        todoService.updateTodo(req, 10L);
    }

    @Test
    public void deleteById_test(){
        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(getEntity()));
        doNothing().when(todoRepository).deleteById(anyLong());
        todoService.deleteById(1L);
        verify(todoRepository, times(1)).deleteById(1L);
    }

    @Test(expected = TodoNotFoundException.class)
    public void deleteById_test_invalid(){
        todoService.deleteById(1L);
        verify(todoRepository, times(1)).deleteById(1L);
    }

    private TodoRequest getReq(){
        return new TodoRequest("test1", false);
    }

    private Todo getEntity(){
        return new Todo(1L, "test1", false);
    }
}
