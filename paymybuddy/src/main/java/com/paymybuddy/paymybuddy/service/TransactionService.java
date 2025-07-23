package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Transaction executeTransfer(Long senderId, Long receiverId, Double amount, String description) {
        User sender = userRepo.findById(senderId)
            .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = userRepo.findById(receiverId)
            .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        if (sender.getBalance() < amount) {
            throw new IllegalStateException("Solde insuffisant");
        }
        // Calcul des frais pour la monétisation (0,5 %)
        double fee = amount * 0.005;
        double total = amount + fee;

        sender.setBalance(sender.getBalance() - total);
        receiver.setBalance(receiver.getBalance() + amount);

        // Sauvegarde des profils utilisateurs modifiés
        userRepo.save(sender);
        userRepo.save(receiver);

        // Création de l'entité transaction
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
