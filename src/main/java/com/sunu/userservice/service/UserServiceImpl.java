package com.sunu.userservice.service;

import com.sunu.userservice.dto.UserDto;
import com.sunu.userservice.jpa.UserEntity;
import com.sunu.userservice.jpa.UserRepository;
import com.sunu.userservice.vo.ResponseOrder;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    @Override
    public UserDto getUserByUserId(String userId) {
       UserEntity userEntity =  userRepository.findByUserId(userId);

       if(userEntity == null )
           throw new UsernameNotFoundException("User not found");

       UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

       List<ResponseOrder> orders = new ArrayList<>();
       userDto.setOrders(orders);

       return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);

        userEntity.setEncryptPwd(bCryptPasswordEncoder.encode(userDto.getPwd()));

        userRepository.save(userEntity);

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);
        return returnUserDto;
    }
}
