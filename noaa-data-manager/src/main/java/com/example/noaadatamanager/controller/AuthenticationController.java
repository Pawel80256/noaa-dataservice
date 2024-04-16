package com.example.noaadatamanager.controller;

import com.example.noaadatamanager.dtos.input.UserInputDto;
import com.example.noaadatamanager.dtos.output.JwtResponse;
import com.example.noaadatamanager.entities.User;
import com.example.noaadatamanager.service.JwtService;
import com.example.noaadatamanager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    private final UserService userService;
    private final JwtService jwtService;

    public AuthenticationController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> getToken(@RequestBody UserInputDto userInputDto){
        User user = userService.authenticateUser(userInputDto);
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
