package com.interceptorloging.logging.controller;

import com.interceptorloging.logging.model.User;
import com.interceptorloging.logging.request.GetUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    @PostMapping("/getUserById")
    public ResponseEntity<User> getUser(@RequestBody GetUserRequest request/*,
                                        @RequestHeader(name = "x-session-token") String token*/) {

        return ResponseEntity.ok(User.builder().id("1").name("osman").surname("sevinc").build());
    }
}
