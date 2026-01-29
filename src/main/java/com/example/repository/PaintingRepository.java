package com.example.repository;

import com.example.entity.Painting;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaintingRepository extends JpaRepository<Painting, Long> {

	@Query("SELECT MIN(p.price) FROM Painting p")
    BigDecimal findMinPrice();
	@Query("SELECT MAX(p.price) FROM Painting p")
    BigDecimal findMaxPrice();
}