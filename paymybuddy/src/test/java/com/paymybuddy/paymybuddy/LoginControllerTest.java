package com.paymybuddy.paymybuddy;

import com.paymybuddy.paymybuddy.controller.LoginController;
import com.paymybuddy.paymybuddy.dto.UserSignupDTO;
import com.paymybuddy.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LoginControllerTest {
	@Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private LoginController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginPage_ShouldReturnLoginView() {
        String view = controller.loginPage(null, null, model);
        assertEquals("login", view);
    }

    @Test
    void signupForm_ShouldReturnSignupView() {
        String view = controller.signupForm(model);
        assertEquals("signup", view);
        verify(model).addAttribute(eq("userSignupDTO"), any(UserSignupDTO.class));
    }

    @Test
    void register_ShouldRedirect_WhenValid() {
        UserSignupDTO dto = new UserSignupDTO();
        when(bindingResult.hasErrors()).thenReturn(false);

        String view = controller.register(dto, bindingResult);

        assertEquals("redirect:/login?registered", view);
        verify(userService).register(dto);
    }

    @Test
    void register_ShouldReturnSignup_WhenInvalid() {
        UserSignupDTO dto = new UserSignupDTO();
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = controller.register(dto, bindingResult);

        assertEquals("signup", view);
        verify(userService, never()).register(any());
    }
}
