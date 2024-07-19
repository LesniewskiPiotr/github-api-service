package com.example.unit.service;

import com.example.client.GitHubClient;
import com.example.exception.UserException;
import com.example.model.User;
import com.example.model.dto.UserDTO;
import com.example.service.CalculationService;
import com.example.service.DatabaseService;
import com.example.service.GitHubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubServiceTest {

    @Mock
    private DatabaseService databaseService;

    @Mock
    private CalculationService calculationService;

    @Mock
    private GitHubClient gitHubClient;

    @InjectMocks
    private GitHubService gitHubService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setLogin("testuser");
    }

    @Test
    void getUserData_UserFound_Success() {
        when(gitHubClient.getUser(anyString())).thenReturn(user);
        when(calculationService.calculate(any(User.class))).thenReturn(new BigDecimal(5));

        UserDTO result = gitHubService.getUserData("testuser");

        assertNotNull(result);
        verify(gitHubClient, times(1)).getUser("testuser");
        verify(calculationService, times(1)).calculate(user);
        verify(databaseService, times(1)).incrementRequestCount("testuser");
    }

    @Test
    void getUserData_UserNotFound_ThrowsException() {
        when(gitHubClient.getUser(anyString())).thenReturn(null);

        assertThrows(UserException.class, () -> gitHubService.getUserData("nonexistentuser"));
        verify(gitHubClient, times(1)).getUser("nonexistentuser");
        verify(calculationService, times(0)).calculate(any(User.class));
        verify(databaseService, times(0)).incrementRequestCount(anyString());
    }

    @Test
    void getUserData_ResourceNotFoundException_ThrowsUserNotFoundException() {
        when(gitHubClient.getUser(anyString())).thenThrow(new UserException(HttpStatus.NOT_FOUND, "Resource not found"));

        assertThrows(UserException.class, () -> gitHubService.getUserData("testuser"));
        verify(gitHubClient, times(1)).getUser("testuser");
        verify(calculationService, times(0)).calculate(any(User.class));
        verify(databaseService, times(0)).incrementRequestCount(anyString());
    }
}
