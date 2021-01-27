package com.ning.modules.system.controller;

import com.ning.modules.system.domain.User;
import com.ning.modules.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Validated @RequestBody User resource) {
        userService.create(resource);
        Map<String, String> result = new HashMap<>();
        result.put("code", "201");
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

}
