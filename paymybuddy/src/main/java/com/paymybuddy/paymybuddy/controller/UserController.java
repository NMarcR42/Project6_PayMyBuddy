package com.paymybuddy.paymybuddy.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paymybuddy.paymybuddy.dto.UserProfileDTO;
import com.paymybuddy.paymybuddy.dto.UserSignupDTO;
import com.paymybuddy.paymybuddy.exception.DuplicateEmailException;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import com.paymybuddy.paymybuddy.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
	private final UserService userService;
	private final UserRepository userRepo;

	public UserController(UserService userService, UserRepository userRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
    }

	@PostMapping("/signup")
	public String signup(@Valid UserSignupDTO dto, BindingResult bindingResult,Model model,RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
	        model.addAttribute("userSignupDTO", dto);
	        return "signup";  // 
	    }
		try {
	        userService.register(dto);
	        redirectAttributes.addFlashAttribute("success", "Compte créé avec succès !");
	        return "redirect:/login";
	    } catch (DuplicateEmailException e) {
	        model.addAttribute("errorMessage", e.getMessage());
	        model.addAttribute("user", dto);
	        return "signup"; 
	    }
	}

    
    @GetMapping("/profil")
    public String showProfile(Model model, Principal principal) {
    	model.addAttribute("page", "profil");
        Optional<User> userOpt = userRepo.findByEmail(principal.getName());
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }
        model.addAttribute("user", userOpt.get());
        return "profil";  // fichier html
    }
    
    @GetMapping("/relation/add")
    public String showAddRelationPage(Model model) {
        model.addAttribute("page", "ajoutRelation");
        return "ajoutRelation"; // fichier html
    }
    
    @PostMapping("/relation/add")
    public String addRelation(@RequestParam String relationEmail,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {
        try {
            userService.addRelation(principal.getName(), relationEmail);
            redirectAttributes.addFlashAttribute("success", "Relation ajoutée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur : " + e.getMessage());
        }
        return "redirect:/users/relation/add";
    }
    
    @PostMapping("/update-password")
    public String updatePassword(@RequestParam String newPassword,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        try {
            userService.updatePassword(principal.getName(), newPassword);
            redirectAttributes.addFlashAttribute("passwordMessage", "Mot de passe modifié avec succès !");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("passwordError", e.getMessage());
        }
        return "redirect:/users/profil";
    }
}
