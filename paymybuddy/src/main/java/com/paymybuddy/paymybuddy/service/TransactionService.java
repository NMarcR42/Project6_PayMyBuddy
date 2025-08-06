package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository txRepo;
    private final UserRepository userRepo;

    public TransactionService(TransactionRepository txRepo, UserRepository userRepo) {
        this.txRepo = txRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public Transaction executeTransfer(int senderId, int receiverId, BigDecimal amount, String description) {
        User sender = userRepo.findById(senderId)
            .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = userRepo.findById(receiverId)
            .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        // Vérification solde insuffisant
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Solde insuffisant");
        }

        // Calcul des frais 0.5%
        BigDecimal fee = amount.multiply(new BigDecimal("0.005")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = amount.add(fee);

        sender.setBalance(sender.getBalance().subtract(total));
        receiver.setBalance(receiver.getBalance().add(amount));

        userRepo.save(sender);
        userRepo.save(receiver);

        Transaction tx = new Transaction();
        tx.setSender(sender);
        tx.setReceiver(receiver);
        tx.setAmount(amount);
        tx.setFee(fee);
        tx.setDescription(description);
        tx.setStatus("COMPLETED");
        tx.setCreatedAt(LocalDateTime.now());
        
        

        return txRepo.save(tx);
        
    }
}
