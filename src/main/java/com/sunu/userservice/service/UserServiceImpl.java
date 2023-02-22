package com.sunu.userservice.service;

import com.sunu.userservice.dto.UserDto;
import com.sunu.userservice.jpa.UserEntity;
import com.sunu.userservice.jpa.UserRepository;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptPwd("encrypted_password");


        userRepository.save(userEntity);

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);
        return returnUserDto;
    }
}
