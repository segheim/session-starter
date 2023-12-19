package com.example.starter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    private Long id;
    private String login;
    private LocalDateTime createAt;

}
