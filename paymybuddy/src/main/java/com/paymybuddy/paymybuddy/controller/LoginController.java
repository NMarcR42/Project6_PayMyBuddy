package com.paymybuddy.paymybuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paymybuddy.paymybuddy.dto.UserSignupDTO;
import com.paymybuddy.paymybuddy.service.UserService;

import jakarta.validation.Valid;

@Controller
public class LoginController {
	private final UserService userService;
	
	public LoginController(UserService userService) {
        this.userService = userService;
    }
	
	@GetMapping("/login")
    public String loginPage(@RequestParam(required=false) String error, @RequestParam(required=false) String logout,Model model) {
        if (error != null) model.addAttribute("loginError", true);
        if (logout != null) model.addAttribute("logoutSuccess", true);
        return "login";
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("userSignupDTO", new UserSignupDTO());
        return "signup";
    }

    @PostMapping("/signup")
    public String register(@ModelAttribute @Valid UserSignupDTO dto,
                           BindingResult br) {
        if (br.hasErrors()) return "signup";
        userService.register(dto);
        return "redirect:/login?registered";
    }
}
