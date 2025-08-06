package com.paymybuddy.paymybuddy;
import com.paymybuddy.paymybuddy.controller.UserController;
import com.paymybuddy.paymybuddy.dto.UserSignupDTO;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import com.paymybuddy.paymybuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    void showProfile_userFound_shouldReturnProfilView() throws Exception {
        User user = new User();
        user.setEmail("test@mail.com");

        when(userRepo.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        Principal principal = () -> "test@mail.com";

        mockMvc.perform(get("/users/profil").principal(principal))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("profil"));
    }

    @Test
    void updatePassword_success() throws Exception {
        doNothing().when(userService).updatePassword("test@mail.com", "newpass");

        Principal principal = () -> "test@mail.com";

        mockMvc.perform(post("/users/update-password")
                        .principal(principal)
                        .param("newPassword", "newpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profil"));
    }
}