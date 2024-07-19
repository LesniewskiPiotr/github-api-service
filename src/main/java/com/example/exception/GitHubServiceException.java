package com.example.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class GitHubServiceException extends ResponseStatusException {
    public GitHubServiceException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
