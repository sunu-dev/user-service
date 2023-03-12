package com.sunu.userservice.controller;

import com.sunu.userservice.dto.UserDto;
import com.sunu.userservice.jpa.UserEntity;
import com.sunu.userservice.service.UserService;
import com.sunu.userservice.vo.Greeting;
import com.sunu.userservice.vo.RequestUser;
import com.sunu.userservice.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user-service/")
public class UserController {

    private Environment env;
    private UserService userService;

    @Autowired
    private Greeting greeting;

    @Autowired
    public UserController(Environment env, UserService userService) {

        this.env = env;
        this.userService = userService;
    }

    @GetMapping("health_check")
    public String status(){

        return String.format("It's Woriking in User Serivice on PORT %s", env.getProperty("local.server.port"));
    }
    @GetMapping("welcome")
    public String welcome(){
        //return env.getProperty("greeting.message");
        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody RequestUser user){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);

    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers(){

        Iterable<UserEntity> userList = userService.getUserByAll();

        List<ResponseUser> result = new ArrayList<>();

        userList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseUser.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);

    }


    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUsers(@PathVariable("userId") String userId){

        UserDto userDto = userService.getUserByUserId(userId);

        ResponseUser returnValue = new ModelMapper().map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);

    }




}

