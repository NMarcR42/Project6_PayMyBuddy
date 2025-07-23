package com.paymybuddy.paymybuddy.dto;

import com.paymybuddy.paymybuddy.model.User;
import lombok.Getter;

@Getter
public class UserProfileDTO {
    private int userId;
    private String username;
    private String email;

    public UserProfileDTO(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    public static UserProfileDTO from(User user) {
        return new UserProfileDTO(user);
    }
}
