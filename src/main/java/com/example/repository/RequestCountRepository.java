package com.example.repository;

import com.example.model.RequestCount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RequestCountRepository extends JpaRepository<RequestCount, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT rc FROM RequestCount rc WHERE rc.login = :login")
    Optional<RequestCount> findByLoginForUpdate(@Param("login") String login);
}