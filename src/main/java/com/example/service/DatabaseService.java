package com.example.service;

import com.example.model.RequestCount;
import com.example.repository.RequestCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseService {

    private final RequestCountRepository requestCountRepository;

    @Transactional
    public void incrementRequestCount(String login) {
        Optional<RequestCount> optionalRequestCount = requestCountRepository.findByLoginForUpdate(login);
        RequestCount requestCount = getOrCreateRequestCount(optionalRequestCount, login);
        requestCountRepository.saveAndFlush(requestCount);
    }

    private RequestCount getOrCreateRequestCount(Optional<RequestCount> optionalRequestCount, String login) {
        RequestCount requestCount;
        if (optionalRequestCount.isPresent()) {
            requestCount = optionalRequestCount.get();
            requestCount.addCount();
        } else {
            requestCount = RequestCount.builder()
                    .login(login)
                    .count(1)
                    .build();
        }
        return requestCount;
    }
}
