package com.paymybuddy.paymybuddy;

import com.paymybuddy.paymybuddy.dto.UserSignupDTO;
import com.paymybuddy.paymybuddy.exception.DuplicateEmailException;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import com.paymybuddy.paymybuddy.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

	private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void register_ShouldCreateUser_WhenEmailIsUnique() {
        UserSignupDTO dto = new UserSignupDTO();
        dto.setEmail("test@example.com");
        dto.setUsername("John");
        dto.setPassword("password123");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("hashedPassword");

        User user = userService.register(dto);

        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getUsername(), user.getUsername());
        assertEquals("hashedPassword", user.getPassword());
        assertEquals(BigDecimal.ZERO, user.getBalance());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_ShouldThrowException_WhenEmailIsDuplicate() {

        UserSignupDTO dto = new UserSignupDTO();
        dto.setEmail("duplicate@example.com");
        dto.setUsername("DupUser");
        dto.setPassword("test");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(DuplicateEmailException.class, () -> userService.register(dto));

        verify(userRepository, never()).save(any());
    }
    
    @Test
    void updatePassword_ShouldUpdate_WhenValid() {
        User user = new User();
        user.setEmail("test@mail.com");
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpass")).thenReturn("hashed");

        userService.updatePassword("test@mail.com", "newpass");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("hashed", captor.getValue().getPassword());
    }
    
    @Test
    void updatePassword_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findByEmail("unknown@mail.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> userService.updatePassword("unknown@mail.com", "newpassword"));
        verify(userRepository, never()).save(any());
    }
}
