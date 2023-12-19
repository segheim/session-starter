package com.example.session.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionDto {

    private Long id;
    private String login;
    private LocalDateTime createAt;

}
