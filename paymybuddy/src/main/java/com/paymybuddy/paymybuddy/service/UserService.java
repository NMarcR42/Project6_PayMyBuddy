package com.paymybuddy.paymybuddy.service;

import org.springframework.stereotype.Service;

import com.paymybuddy.paymybuddy.dto.UserSignupDTO;
import com.paymybuddy.paymybuddy.exception.DuplicateEmailException;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;

import jakarta.annotation.PostConstruct;

import java.math.BigDecimal;

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
    	if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email déjà utilisé : " + dto.getEmail());
        }
    	
    	 User user = new User();
    	    user.setEmail(dto.getEmail());
    	    user.setUsername(dto.getUsername());

    	    // Encodage du mot de passe
    	    String encodedPassword = passwordEncoder.encode(dto.getPassword());
    	    user.setPassword(encodedPassword);

    	    user.setBalance(BigDecimal.ZERO);

    	    userRepository.save(user);

    	    return user;  
    }
    
    public void addRelation(String currentUserEmail, String relationEmail) {
        if (currentUserEmail.equalsIgnoreCase(relationEmail)) {
            throw new IllegalArgumentException("Vous ne pouvez pas vous ajouter vous-même.");
        }

        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur connecté introuvable"));

        User relation = userRepository.findByEmail(relationEmail)
                .orElseThrow(() -> new IllegalArgumentException("L'utilisateur avec cet email n'existe pas"));

        if (currentUser.getConnections().contains(relation)) {
            throw new IllegalArgumentException("Cet utilisateur est déjà dans vos relations.");
        }

        currentUser.getConnections().add(relation);
        userRepository.save(currentUser);
    }
    
    public void updatePassword(String email, String newPassword) {
        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("Le mot de passe doit faire au moins 6 caractères.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
