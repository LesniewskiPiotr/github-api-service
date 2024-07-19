package com.example.unit.controller;

import com.example.controller.UserController;
import com.example.exception.UserException;
import com.example.model.dto.UserDTO;
import com.example.service.GitHubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitHubService gitHubService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUser_success() throws Exception {
        String login = "testUser";
        UserDTO userDTO = new UserDTO(null, login, null, null, null, null, BigDecimal.ONE);

        when(gitHubService.getUserData(login)).thenReturn(userDTO);

        mockMvc.perform(get("/users/{login}", login)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(login));
    }

    @Test
    public void testGetUser_notFound() throws Exception {
        String login = "testUser";

        when(gitHubService.getUserData(login)).thenThrow(new UserException(HttpStatus.NOT_FOUND, "User not found"));

        mockMvc.perform(get("/users/{login}", login)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"message\":\"User not found\",\"status\":\"404\"}"));
    }
}
