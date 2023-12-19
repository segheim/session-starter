package com.example.usertest.controller;

import com.example.starter.annotation.SessionProvider;
import com.example.starter.model.Session;
import com.example.usertest.dto.UserDto;
import com.example.usertest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @SessionProvider(blackList = {"Danny", "Li"})
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto, Session session) {
        log.info(session.toString());
        UserDto user = service.save(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable("id") Long id) {
        UserDto user = service.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, Session session) {
        log.info(session.toString());
        UserDto user = service.update(userDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
