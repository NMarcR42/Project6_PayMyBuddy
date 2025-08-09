package com.paymybuddy.paymybuddy;
import com.paymybuddy.paymybuddy.controller.UserController;
import com.paymybuddy.paymybuddy.dto.UserSignupDTO;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import com.paymybuddy.paymybuddy.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import java.security.Principal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepo;
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@mail.com");
        testUser.setUserId(1);
    }

    @Test
    void showProfile_userFound_shouldReturnProfilView() throws Exception {
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/users/profil")
                .with(user("test@mail.com")))  
            .andExpect(status().isOk());
    }

    @Test
    void updatePassword_success() throws Exception {
        mockMvc.perform(post("/users/update-password")
                .with(user("test@mail.com"))  
                .with(csrf())                 
                .param("newPassword", "newStrongPassword123"))
            .andExpect(status().is3xxRedirection());
    }
}