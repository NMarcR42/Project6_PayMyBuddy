package com.paymybuddy.paymybuddy.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.paymybuddy.dto.UserProfileDTO;
import com.paymybuddy.paymybuddy.dto.UserSignupDTO;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserProfileDTO> signup(
            @RequestBody @Valid UserSignupDTO dto) {
        User user = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserProfileDTO.from(user));
    }
}
