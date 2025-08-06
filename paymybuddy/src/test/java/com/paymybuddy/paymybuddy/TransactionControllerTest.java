package com.paymybuddy.paymybuddy;

import com.paymybuddy.paymybuddy.controller.TransactionController;
import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import com.paymybuddy.paymybuddy.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService txService;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private TransactionRepository txRepo;

    @Test
    void showTransferPage_shouldReturnTransfertView() throws Exception {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setConnections(Collections.emptySet());

        when(userRepo.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(txRepo.findBySender(user)).thenReturn(Collections.emptyList());

        Principal principal = () -> "test@mail.com";

        mockMvc.perform(get("/transfer").principal(principal))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("currentUser"))
                .andExpect(view().name("transfert"));
    }

    @Test
    void handleTransfer_successRedirect() throws Exception {
        User sender = new User();
        sender.setUserId(1);
        sender.setEmail("test@mail.com");

        when(userRepo.findByEmail("test@mail.com")).thenReturn(Optional.of(sender));
        doNothing().when(txService).executeTransfer(1, 2, BigDecimal.valueOf(100), "test");

        Principal principal = () -> "test@mail.com";

        mockMvc.perform(post("/transfer")
                        .principal(principal)
                        .param("receiverId", "2")
                        .param("amount", "100")
                        .param("description", "test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transfer?success=Transfert%20réussi"));
    }
}