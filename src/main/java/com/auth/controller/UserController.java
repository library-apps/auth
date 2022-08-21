package com.auth.controller;

import com.auth.dto.UserDto;
import com.auth.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @SneakyThrows(Exception.class)
    @PostMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createUser(@RequestBody UserDto.CreateUserDto dto){
        log.info("POST /api/v1/users is called");
        return userService.createUser(dto);
    }

    @SneakyThrows(Exception.class)
    @PostMapping(path = "/setrole", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> setRoleToUser(@RequestBody UserDto.SetRoleDto dto){
        log.info("POST /api/v1/users is called");
        return userService.setRoleToUser(dto);
    }

    @SneakyThrows(Exception.class)
    @GetMapping(path = "/users/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUser(@PathVariable(name = "username") String username) {

        log.info("GET http://localhost:8080/api/v1/student/{} is called", username);
        return userService.getUserResponse(username);
    }
}
