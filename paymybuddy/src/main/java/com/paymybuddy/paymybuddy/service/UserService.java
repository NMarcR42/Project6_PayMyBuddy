package com.paymybuddy.paymybuddy.service;

import org.springframework.stereotype.Service;

import com.paymybuddy.paymybuddy.dto.UserSignupDTO;
import com.paymybuddy.paymybuddy.exception.DuplicateEmailException;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(UserSignupDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException(); // email déja utilisé
        }
        // enregistre l'utilisateur si le mail n'est pas déja utilisé.
        User user = new User(dto.getUsername(), dto.getEmail(),
                             passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(user);
    }
}
