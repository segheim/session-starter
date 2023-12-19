package com.example.usertest.service;

import com.example.usertest.dto.UserDto;
import com.example.usertest.mapper.UserMapper;
import com.example.usertest.model.User;
import com.example.usertest.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserDto save(UserDto userDto) {
        User user = repository.save(mapper.fromDto(userDto));
        return mapper.toDto(user);
    }

    public UserDto findById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
        return mapper.toDto(user);
    }

    public UserDto update(UserDto userDto) {
        User user = repository.findById(userDto.getId()).orElseThrow(() -> new NoSuchElementException("User not found"));
        User savedUser = repository.save(user);
        return mapper.toDto(savedUser);
    }

}
