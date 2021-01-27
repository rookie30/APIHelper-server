package com.ning.modules.system.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

//    @PreAuthorize("@ap.check(123)")
//    @GetMapping("/query")
//    public ResponseEntity<Object> query(){
//        return new ResponseEntity<>("123", HttpStatus.OK);
//    }
}
