package com.paymybuddy.paymybuddy;

import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import com.paymybuddy.paymybuddy.service.TransactionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {
	private TransactionRepository txRepo;
    private UserRepository userRepo;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        txRepo = mock(TransactionRepository.class);
        userRepo = mock(UserRepository.class);
        transactionService = new TransactionService(txRepo, userRepo);
    }

    @Test
    void executeTransfer_ShouldTransferFundsAndSaveTransaction_WhenBalanceIsSufficient() {

        User sender = new User();
        sender.setUserId(1);
        sender.setBalance(new BigDecimal("1000.00"));

        User receiver = new User();
        receiver.setUserId(2);
        receiver.setBalance(new BigDecimal("500.00"));

        BigDecimal amount = new BigDecimal("200.00");
        String description = "Paiement test";
        BigDecimal expectedFee = new BigDecimal("1.00"); // 0.5% de 200 = 1.00

        when(userRepo.findById(1)).thenReturn(Optional.of(sender));
        when(userRepo.findById(2)).thenReturn(Optional.of(receiver));
        when(txRepo.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction tx = transactionService.executeTransfer(1, 2, amount, description);


        assertEquals(sender, tx.getSender());
        assertEquals(receiver, tx.getReceiver());
        assertEquals(amount, tx.getAmount());
        assertEquals(expectedFee, tx.getFee());
        assertEquals("COMPLETED", tx.getStatus());
        assertEquals("Paiement test", tx.getDescription());

        assertEquals(new BigDecimal("799.00"), sender.getBalance());
        assertEquals(new BigDecimal("700.00"), receiver.getBalance());

        verify(userRepo).save(sender);
        verify(userRepo).save(receiver);
        verify(txRepo).save(any(Transaction.class));
    }

    @Test
    void executeTransfer_ShouldThrow_WhenSenderNotFound() {
        when(userRepo.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.executeTransfer(1, 2, new BigDecimal("100"), "desc");
        });

        assertEquals("Sender not found", exception.getMessage());
    }

    @Test
    void executeTransfer_ShouldThrow_WhenReceiverNotFound() {
        User sender = new User();
        sender.setUserId(1);
        sender.setBalance(new BigDecimal("500"));
        when(userRepo.findById(1)).thenReturn(Optional.of(sender));
        when(userRepo.findById(2)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.executeTransfer(1, 2, new BigDecimal("100"), "desc");
        });

        assertEquals("Receiver not found", exception.getMessage());
    }

    @Test
    void executeTransfer_ShouldThrow_WhenInsufficientBalance() {
        User sender = new User();
        sender.setUserId(1);
        sender.setBalance(new BigDecimal("50"));

        User receiver = new User();
        receiver.setUserId(2);
        receiver.setBalance(new BigDecimal("500"));

        when(userRepo.findById(1)).thenReturn(Optional.of(sender));
        when(userRepo.findById(2)).thenReturn(Optional.of(receiver));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            transactionService.executeTransfer(1, 2, new BigDecimal("100"), "desc");
        });

        assertEquals("Solde insuffisant", exception.getMessage());
    }
}
