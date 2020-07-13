package com.crud.tasks.controller;

import com.crud.tasks.domain.*;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import com.crud.tasks.trello.client.CreatedTrelloCardDto;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskMapper taskMapper;

    @MockBean
    private DbService dbService;

    @Test
    public void shouldFetchEmptyTasks() throws Exception {
        //Given
        List<TaskDto> taskDtos = new ArrayList<>();
        when(taskMapper.mapToTaskDtoList(dbService.getAllTasks())).thenReturn(taskDtos);
        //When & Then
        mockMvc.perform(get("/v1/task/getTasks").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldFetchTask() throws Exception {
        //Given
        TaskDto taskDto = new TaskDto(100L, "Test", "Testing");
        when(taskMapper.mapToTaskDto(dbService.getTask(100L).orElseThrow(TaskNotFoundException::new))).thenReturn(taskDto);
        //When & Then
        mockMvc.perform(get("/v1/task/getTask").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
              //  .andExpect(jsonPath("$[0].id", is("1")));

    }

    @Test
    public void shouldCreateTask() throws Exception {
        //Given
        TaskDto taskDto = new TaskDto(
                100L, "Test", "Testing");
        Task task = new Task(
                100L, "Test", "Testing");
        /*CreatedTaskDto createdTaskDto = new CreatedTaskDto(
                "123", "Test", "http://test.com");*/
        Gson gson = new Gson();
        String jsonContent = gson.toJson(task);
        when(dbService.saveTask(ArgumentMatchers.any(Task.class))).thenReturn(task);
        //When & Then
        mockMvc.perform(post("/v1/task/createTask")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(jsonPath("$.title", is("Test")))
                .andExpect(jsonPath("$.content", is("Testing")));
    }
}
