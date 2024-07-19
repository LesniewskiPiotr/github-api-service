package com.example.service;

import com.example.model.User;
import com.example.service.CalculationService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculationServiceTest {

    private final CalculationService calculationService = new CalculationService();

    @Test
    public void testCalculate_withFollowers() {
        User user = new User();
        user.setFollowers(3);
        user.setPublicRepos(2);

        BigDecimal result = calculationService.calculate(user);

        assertEquals(new BigDecimal("8.0"), result);
    }

    @Test
    public void testCalculate_withZeroFollowers() {
        User user = new User();
        user.setFollowers(0);
        user.setPublicRepos(2);

        BigDecimal result = calculationService.calculate(user);

        assertEquals(new BigDecimal("0"), result);
    }
}
