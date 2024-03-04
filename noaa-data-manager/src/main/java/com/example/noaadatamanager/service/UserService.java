package com.example.noaadatamanager.service;

import com.example.noaadatamanager.dtos.input.UserInputDto;
import com.example.noaadatamanager.models.User;
import com.example.noaadatamanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserInputDto userInputDto){
        User user = new User(
                userInputDto.getUsername(),
                passwordEncoder.encode(userInputDto.getPassword())
        );

        userRepository.save(user);
    }

}
