package com.example.session.controller;

import com.example.session.dto.SessionDto;
import com.example.session.model.Session;
import com.example.session.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService service;

    @PostMapping
    public ResponseEntity<Long> createSession(@RequestBody Session session) {
        Long sessionId = service.save(session);
        return new ResponseEntity<>(sessionId, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<SessionDto> findByLoginOrCreate(@RequestParam String login) {
        SessionDto session = service.findByLoginOrCreate(login);
        return new ResponseEntity<>(session, HttpStatus.OK);
    }
}
