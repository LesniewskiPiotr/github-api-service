package com.example.controller;

import com.example.model.dto.UserDTO;
import com.example.service.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final GitHubService gitHubService;

    @GetMapping("/{login}")
    public UserDTO getUser(@PathVariable String login) {
        return gitHubService.getUserData(login);
    }

}
