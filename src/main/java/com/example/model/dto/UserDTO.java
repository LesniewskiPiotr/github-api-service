package com.example.model.dto;

import com.example.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserDTO(
        String id,
        String login,
        String name,
        String type,
        String avatarUrl,
        LocalDateTime createdAt,
        BigDecimal calculations
) {

    public static UserDTO toDto(User user){
        return new UserDTO(
                user.getId(),
                user.getLogin(),
                user.getName(),
                user.getType(),
                user.getAvatarUrl(),
                user.getCreatedAt(),
                user.getCalculations()
        );
    }
}
