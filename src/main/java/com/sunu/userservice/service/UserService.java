package com.sunu.userservice.service;

import com.sunu.userservice.dto.UserDto;
import com.sunu.userservice.jpa.UserEntity;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);
    Iterable<UserEntity> getUserByAll();
}
