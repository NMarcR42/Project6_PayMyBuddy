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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.security.test.context.support.WithMockUser;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
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
        User currentUser = new User();
        currentUser.setUserId(1);
        currentUser.setEmail("test@mail.com");
       
        
        when(userRepo.findByEmail("test@mail.com")).thenReturn(Optional.of(currentUser));
        when(txRepo.findBySender(currentUser)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/transfer")
                        .with(user("test@mail.com").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("transfert"))  
                .andExpect(model().attributeExists("currentUser"))
                .andExpect(model().attributeExists("relations"))
                .andExpect(model().attributeExists("transactions"));
    }
    @WithMockUser(username="test@mail.com")
    @Test
    void handleTransfer_successRedirect() throws Exception {
        User sender = new User();
        sender.setUserId(1);
        sender.setEmail("test@mail.com");
        sender.setBalance(new BigDecimal("1000"));

        when(userRepo.findByEmail("test@mail.com")).thenReturn(Optional.of(sender));

        Transaction fakeTransaction = new Transaction();

        when(txService.executeTransfer(
            eq(1),
            eq(2),
            eq(new BigDecimal("100")),
            eq("test description")
        )).thenReturn(fakeTransaction);

        mockMvc.perform(post("/transfer")
                .with(user("test@mail.com"))  
                .with(csrf())                 
                .param("receiverId", "2")
                .param("amount", "100")
                .param("description", "test description"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/transfer?success=Transfert%20réussi"));
    }

}