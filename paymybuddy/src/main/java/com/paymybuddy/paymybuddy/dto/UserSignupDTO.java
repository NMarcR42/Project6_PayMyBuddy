package com.paymybuddy.paymybuddy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupDTO {
	@NotBlank(message = "Username obligatoire")
    @Size(min = 3, max = 50)
    private String username;

    @Email(message = "Email non valide")
    @NotBlank(message = "Email obligatoire")
    @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "Format d'email non conforme")
    private String email;

    @NotBlank
    @Size(min = 6, max = 100, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;
    
	public UserSignupDTO(@NotBlank @Size(min = 3, max = 50) String username, @Email @NotBlank String email,
			@NotBlank @Size(min = 6, max = 100) String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
	public UserSignupDTO() {}
}
