package com.example.service;

import com.example.model.RequestCount;
import com.example.repository.RequestCountRepository;
import com.example.service.DatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DatabaseServiceTest {

    @Mock
    private RequestCountRepository requestCountRepository;

    @InjectMocks
    private DatabaseService databaseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIncrementRequestCount_existingUser() {
        String login = "testUser";
        RequestCount existingCount = new RequestCount();
        existingCount.setLogin(login);
        existingCount.setCount(5);

        when(requestCountRepository.findByLoginForUpdate(login)).thenReturn(Optional.of(existingCount));

        databaseService.incrementRequestCount(login);

        verify(requestCountRepository, times(1)).saveAndFlush(existingCount);
        assertEquals(6, existingCount.getCount());
    }

    @Test
    public void testIncrementRequestCount_newUser() {
        String login = "newUser";

        when(requestCountRepository.findByLoginForUpdate(login)).thenReturn(Optional.empty());

        databaseService.incrementRequestCount(login);

        verify(requestCountRepository, times(1)).saveAndFlush(any(RequestCount.class));
    }
}
