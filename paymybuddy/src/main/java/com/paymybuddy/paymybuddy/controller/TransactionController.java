package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService txService;

    public TransactionController(TransactionService txService) {
        this.txService = txService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestParam Long senderId,@RequestParam Long receiverId,
    		@RequestParam Double amount,@RequestParam(required = false) String description) {
    	
        Transaction tx = txService.executeTransfer(senderId, receiverId, amount, description);
        return ResponseEntity.ok(tx);
    }
}