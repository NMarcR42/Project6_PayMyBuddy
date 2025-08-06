package com.paymybuddy.paymybuddy.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;
    
    @Column(nullable = false)
    private BigDecimal  balance;

    // Relations
    @ManyToMany
    @JoinTable(
        name = "connections",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "connection_id")
    )
    private Set<User> connections;

    @OneToMany(mappedBy = "sender")
    private Set<Transaction> sentTransactions;

    @OneToMany(mappedBy = "receiver")
    private Set<Transaction> receivedTransactions;

	

	public User(int userId, String username, String email, String password, BigDecimal balance, Set<User> connections,
			Set<Transaction> sentTransactions, Set<Transaction> receivedTransactions) {
		super();
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.password = password;
		this.balance = balance;
		this.connections = connections;
		this.sentTransactions = sentTransactions;
		this.receivedTransactions = receivedTransactions;
	}

	
	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
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


	public BigDecimal getBalance() {
		return balance;
	}


	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}


	public Set<User> getConnections() {
		return connections;
	}


	public void setConnections(Set<User> connections) {
		this.connections = connections;
	}


	public Set<Transaction> getSentTransactions() {
		return sentTransactions;
	}


	public void setSentTransactions(Set<Transaction> sentTransactions) {
		this.sentTransactions = sentTransactions;
	}


	public Set<Transaction> getReceivedTransactions() {
		return receivedTransactions;
	}

	public void setReceivedTransactions(Set<Transaction> receivedTransactions) {
		this.receivedTransactions = receivedTransactions;
	}


	public User() {
		
	}

}
