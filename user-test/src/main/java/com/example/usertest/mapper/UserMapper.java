package com.example.usertest.mapper;

import com.example.usertest.dto.UserDto;
import com.example.usertest.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User fromDto(UserDto userDto);

}
