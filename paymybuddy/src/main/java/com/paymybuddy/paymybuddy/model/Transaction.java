package com.paymybuddy.paymybuddy.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal fee;

    @Column(nullable = false)
    private String status = "WAITING";

    @Column(nullable = true)
    private LocalDateTime createdAt = LocalDateTime.now();

	public Transaction(Long transactionId, User sender, User receiver, String description, BigDecimal amount, BigDecimal fee,
			String status, LocalDateTime createdAt) {
		super();
		this.transactionId = transactionId;
		this.sender = sender;
		this.receiver = receiver;
		this.description = description;
		this.amount = amount;
		this.fee = fee;
		this.status = status;
		this.createdAt = createdAt;
	}
	
	public Long getTransactionId() {
		return transactionId;
	}



	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}



	public User getSender() {
		return sender;
	}



	public void setSender(User sender) {
		this.sender = sender;
	}



	public User getReceiver() {
		return receiver;
	}



	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public BigDecimal getAmount() {
		return amount;
	}



	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}



	public BigDecimal getFee() {
		return fee;
	}



	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public LocalDateTime getCreatedAt() {
		return createdAt;
	}



	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}



	public Transaction() {
	    this.status = "WAITING";
	    this.createdAt = LocalDateTime.now();
	}
	

}
