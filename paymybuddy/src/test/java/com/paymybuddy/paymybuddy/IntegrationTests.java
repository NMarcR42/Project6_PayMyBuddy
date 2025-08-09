package com.paymybuddy.paymybuddy;

import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class IntegrationTests {
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
    	user1 = new User();
        user1.setUsername("user1");
        user1.setPassword(passwordEncoder.encode("pass1"));
        user1.setEmail("user@example.com");
        user1.setBalance(new BigDecimal("300.00"));

        userRepository.save(user1);

        user2 = new User();
        user2.setUsername("user2");
        user2.setPassword(passwordEncoder.encode("pass2"));
        user2.setEmail("user2@example.com");
        user2.setBalance(BigDecimal.ZERO);

        userRepository.save(user2);
    }

    @Test
    @WithMockUser(username = "user1")
    void menuShouldBeVisibleOnTransferPage() throws Exception {
        mockMvc.perform(get("/transfer")
                .with(user("user@example.com").roles("USER"))) 
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Pay My Buddy")))
            .andExpect(content().string(containsString("Transfert")));
    }

    @Test
    @WithMockUser(username = "user1")
    void shouldFailTransferIfReceiverNotFound() throws Exception {
        mockMvc.perform(post("/transfer")
                        .param("receiverId", "9999")
                        .param("description", "Erreur transfert")
                        .param("amount", "50"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/transfer?error*"));
    }

    @Test
    @WithMockUser(username = "user1")
    void shouldAddRelationAndTransferSuccessfully() throws Exception { 
        user1.setBalance(new BigDecimal("100.00"));
        userRepository.save(user1);

        user1.getConnections().add(user2);
        userRepository.save(user1);

        mockMvc.perform(post("/transfer")
                .with(user(user1.getEmail()).roles("USER"))
                .param("receiverId", String.valueOf(user2.getUserId()))
                .param("description", "Test transfert")
                .param("amount", "10.00"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("/transfer?success=*"));
    }
}
