package com.example.service;

import com.example.client.GitHubClient;
import com.example.exception.GitHubServiceException;
import com.example.model.User;
import com.example.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.model.dto.UserDTO.toDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubService {

    private final DatabaseService databaseService;
    private final CalculationService calculationService;
    private final GitHubClient gitHubClient;

    @Transactional
    public UserDTO getUserData(String login) {
        User user = gitHubClient.getUser(login);
        if (user == null) {
            throw new GitHubServiceException(HttpStatus.NOT_FOUND, String.format("User not found for login: %s", login));
        }
        user.setCalculations(calculationService.calculate(user));
        databaseService.incrementRequestCount(login);
        return toDto(user);
    }
}
