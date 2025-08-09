package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import com.paymybuddy.paymybuddy.service.TransactionService;
import com.paymybuddy.paymybuddy.model.User;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller 
@RequestMapping("/transfer")
public class TransactionController {
	private final TransactionService txService;
    private final UserRepository userRepo;
    private final TransactionRepository txRepo;

    public TransactionController(TransactionService txService, UserRepository userRepo, TransactionRepository txRepo) {
        this.txService = txService;
        this.userRepo = userRepo;
        this.txRepo = txRepo;
    }
    
    @GetMapping
    public String showTransferPage(Model model,
            Principal principal,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "success", required = false) String success) {
    		
    	model.addAttribute("page", "transfer");
    	User currentUser = userRepo.findByEmail(principal.getName())
    	        .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + principal.getName()));
		model.addAttribute("currentUser", currentUser);
		
		// Relations de l'utilisateur
		List<User> relations = new ArrayList<>(currentUser.getConnections()); 
		model.addAttribute("relations", relations);
		
		// Historique des transferts
		List<Transaction> transactions = txRepo.findBySender(currentUser);
		model.addAttribute("transactions", transactions);
		
		if (error != null) model.addAttribute("error", error);
		if (success != null) model.addAttribute("success", success);
		
		return "transfert"; // Nom du fichier HTML
    }
    
    /**
     * Effectue un transfert
     */
    @PostMapping
    public String handleTransfer(@RequestParam int receiverId,
                                 @RequestParam BigDecimal amount,
                                 @RequestParam(required = false) String description,
                                 Principal principal) {

    	Optional<User> senderOpt = userRepo.findByEmail(principal.getName());

        if (senderOpt.isEmpty()) {
            return "redirect:/transfer?error=Utilisateur%20non%20trouvé";
        }

        User sender = senderOpt.get();

        try {
            txService.executeTransfer(sender.getUserId(), receiverId, amount, description);
            String successMessage = URLEncoder.encode("Transfert réussi", StandardCharsets.UTF_8);
            return "redirect:/transfer?success=" + successMessage;
        } catch (IllegalStateException | IllegalArgumentException e) {
            String errorMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/transfer?error=" + errorMessage;
        }
    }
}