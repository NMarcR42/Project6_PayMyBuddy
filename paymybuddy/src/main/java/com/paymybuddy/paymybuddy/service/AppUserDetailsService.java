package com.paymybuddy.paymybuddy.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {
	private final UserRepository repo;
    public AppUserDetailsService(UserRepository repo) { this.repo = repo; }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      User u = repo.findByEmail(email)
           .orElseThrow(() -> new UsernameNotFoundException("User not found"));
      return new org.springframework.security.core.userdetails.User(
          u.getEmail(), u.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
