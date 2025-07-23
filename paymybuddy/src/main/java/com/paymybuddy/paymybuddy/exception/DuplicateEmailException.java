package com.paymybuddy.paymybuddy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Erreur lorsqu'un utilisateur s'enregistre avec un email déjà existant.
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Email déjà utilisé")
public class DuplicateEmailException extends RuntimeException {
	
    public DuplicateEmailException() {
        super("Email déjà utilisé");
    }
    public DuplicateEmailException(String message) {
        super(message);
    }

}
