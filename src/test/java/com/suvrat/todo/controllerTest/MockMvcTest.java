package com.suvrat.todo.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suvrat.todo.pojo.ToDoResponseList;
import com.suvrat.todo.pojo.TodoResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MockMvcTest {
    public static final String API_ROOT = "/todos";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    String body = """
                {
                    "title": "wash floor",
                    "completed": false
                }
                """;

    @Test
    public void shouldFetchTodos() throws Exception {
        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("clean kitchen")));
    }

    @Test
    public void shouldCreateNewTodo() throws Exception {
        var result = mockMvc.perform(
                        post(API_ROOT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body.getBytes())
                                .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andReturn();
        var resultTodo = objectMapper.readValue(result.getResponse().getContentAsString(), TodoResponse.class);
        assertThat(resultTodo.completed()).isFalse();
    }

    @Test
    public void shouldBeAbleToGetById() throws Exception {
        var newTodoRequest = mockMvc.perform(
                        post(API_ROOT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body.getBytes())
                                .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andReturn();
        var newTodo = objectMapper.readValue(newTodoRequest.getResponse().getContentAsString(),
                TodoResponse.class);
        var readBackTodo = mockMvc.perform(
                        get(API_ROOT+"/"+newTodo.id()))
                .andReturn();
        var resultTodo = objectMapper.readValue(readBackTodo.getResponse().getContentAsString(),
                TodoResponse.class);
        assertEquals(newTodo.title(), resultTodo.title());
    }

    @Test
    public void shouldBeAbleToGetById_invalid() throws Exception {
        mockMvc.perform(
                        post(API_ROOT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body.getBytes())
                                .characterEncoding("utf-8"))
                .andExpect(status().isCreated());
        mockMvc.perform(get(API_ROOT+"/"+100))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldBeAbleToGetByTitle() throws Exception {
        var newTodoRequest = mockMvc.perform(
                        post(API_ROOT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body.getBytes())
                                .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andReturn();
        var newTodo = objectMapper.readValue(newTodoRequest.getResponse().getContentAsString(),
                TodoResponse.class);
        var readBackTodo = mockMvc.perform(
                        get(API_ROOT+"/title/"+newTodo.title()))
                .andReturn();
        var resultTodo = objectMapper.readValue(readBackTodo.getResponse().getContentAsString(),
                ToDoResponseList.class);
        assertFalse(resultTodo.todos().isEmpty());
    }

    @Test
    public void shouldBeUpdate() throws Exception {
        var newTodoRequest = mockMvc.perform(
                        post(API_ROOT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body.getBytes())
                                .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andReturn();
        var newTodo = objectMapper.readValue(newTodoRequest.getResponse().getContentAsString(),
                TodoResponse.class);
        var updateBody = """
                {
                    "title": "wash floor 1234",
                    "completed": false
                }
                """;
        var readBackTodo = mockMvc.perform(
                        put(API_ROOT+"/"+newTodo.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateBody.getBytes())
                                .characterEncoding("utf-8"))
                .andReturn();
        var resultTodo = objectMapper.readValue(readBackTodo.getResponse().getContentAsString(),
                TodoResponse.class);
        assertNotEquals(newTodo.title(), resultTodo.title());
        assertEquals(newTodo.id(), resultTodo.id());
    }

    @Test
    public void shouldBeUpdate_invalid() throws Exception {
        mockMvc.perform(
                        post(API_ROOT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body.getBytes())
                                .characterEncoding("utf-8"))
                .andExpect(status().isCreated());
        var updateBody = """
                {
                    "title": "wash floor 1234",
                    "completed": false
                }
                """;
        mockMvc.perform(
                        put(API_ROOT+"/"+10)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateBody.getBytes())
                                .characterEncoding("utf-8"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldBeAbleToDeleteById() throws Exception {
        var newTodoRequest = mockMvc.perform(
                        post(API_ROOT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body.getBytes())
                                .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andReturn();
        var newTodo = objectMapper.readValue(newTodoRequest.getResponse().getContentAsString(),
                TodoResponse.class);
        mockMvc.perform(delete(API_ROOT+"/"+newTodo.id())).andExpect(status().isOk());
    }

    @Test
    public void shouldBeAbleToDeleteAllExistingTodos() throws Exception {
        mockMvc.perform(delete(API_ROOT)).andExpect(status().isOk());
    }

    @Test
    public void shouldBeAbleToDeleteById_invalid() throws Exception {
        mockMvc.perform(
                        post(API_ROOT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body.getBytes())
                                .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andReturn();
        mockMvc.perform(delete(API_ROOT+"/"+10)).andExpect(status().isNotFound());
    }
}
