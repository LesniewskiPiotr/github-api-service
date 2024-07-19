package com.example.integration;

import com.example.model.RequestCount;
import com.example.repository.RequestCountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

@SpringBootTest
public class UserControllerIT {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RequestCountRepository requestCountRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        requestCountRepository.deleteAll();
    }

    @Test
    public void testGetUser_success() throws Exception {
        String login = "LesniewskiPiotr";

        mockMvc.perform(get("/users/{login}", login)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(login));

        Optional<RequestCount> requestCount = requestCountRepository.findById(login);
        assertTrue(requestCount.isPresent());
        assertEquals(1, requestCount.get().getCount());
    }

    @Test
    public void testGetUser_notFound() throws Exception {
        String login = "nonexistentUser";

        mockMvc.perform(get("/users/{login}", login)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found for login: nonexistentUser"));

        Optional<RequestCount> requestCount = requestCountRepository.findById(login);
        assertFalse(requestCount.isPresent());
    }
}
