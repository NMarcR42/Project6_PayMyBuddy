package com.paymybuddy.paymybuddy;


import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import com.paymybuddy.paymybuddy.controller.HomeController;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomeControllerTest {
    private final HomeController controller = new HomeController();

    @Test
    void homePage_ShouldReturnHomeView() {
        String view = controller.homePage();
        assertEquals("home", view);
    }
}
