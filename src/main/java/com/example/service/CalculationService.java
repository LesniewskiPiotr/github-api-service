package com.example.service;

import com.example.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CalculationService {

    public BigDecimal calculate(User user) {
        Integer followers = user.getFollowers();
        if (followers == 0) {
            return BigDecimal.ZERO;
        }
        Integer publicRepos = user.getPublicRepos();
        return BigDecimal.valueOf((6.0 / followers) * (2 + publicRepos));
    }
}
