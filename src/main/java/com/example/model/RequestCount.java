package com.example.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@SuperBuilder
public class RequestCount {
    @Id
    private String login;
    @Column(nullable = false)
    private int count;

    public void addCount() {
       count++;
    }
}
