package com.example.client;

import com.example.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "githubClient", url = "${github.api.url}")
public interface GitHubClient {

    @GetMapping("/{login}")
    User getUser(@PathVariable("login") String login);
}
