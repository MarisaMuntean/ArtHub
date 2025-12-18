package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "paintings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Painting {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String artist;

	@Column(nullable = false)
	private int period;
	
	@Column(nullable = false)
	private String technique;

	@Column(nullable = false)
	private String material;


}