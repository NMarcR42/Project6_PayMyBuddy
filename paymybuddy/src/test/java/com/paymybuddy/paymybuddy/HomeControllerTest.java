package com.paymybuddy.paymybuddy;


import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import com.paymybuddy.paymybuddy.controller.HomeController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HomeControllerTest {
    private final HomeController controller = new HomeController();

    @Test
    void homePage_ShouldReturnHomeView() {
    	Model model = mock(Model.class);
        String view = controller.homePage(model);
        assertEquals("home", view);
        verify(model).addAttribute("page", "home");
    }
}
