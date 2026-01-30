package com.example.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "painting_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Painting painting;
	
	@Column(nullable = false)
	private String address;
	
	@Column(nullable = false)
	private String phoneNumber;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	private LocalDateTime orderDate;
	
	@PrePersist
	public void prePersist() {
        if (this.status == null) {
            this.status = OrderStatus.PENDING;
        }
        if (this.orderDate == null) {
            this.orderDate = LocalDateTime.now();
        }
    }
	
	
}
