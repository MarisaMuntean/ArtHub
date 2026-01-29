package com.example.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.entity.Painting;
import com.example.repository.PaintingRepository;

@Component
public class PaintingDaoService {
	@Autowired
	private PaintingRepository repository;

	public List<Painting> findAllPaintings() {
		return repository.findAll();
	}

	public Painting findById(long id) {
		Optional<Painting> optionalPainting = repository.findById(id);
		return optionalPainting.orElse(null);
	}

	public Painting savePainting(Painting painting) {
		return repository.save(painting);
	}

	public boolean deleteById(long id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
			return true;
		}

		return false;
	}
	
	public BigDecimal getMinPrice() {
        BigDecimal min = repository.findMinPrice();
        return (min != null) ? min : new BigDecimal("0.0");
    }
	
	public BigDecimal getMaxPrice() {
        BigDecimal max = repository.findMaxPrice();
        return (max != null) ? max : new BigDecimal("1000.0"); // default value
    }

	public List<Painting> filterAdvanced(String artist, String periodStr, String technique, String material, BigDecimal minPrice, BigDecimal maxPrice) {

		List<Painting> allPaintings = repository.findAll();

		return allPaintings.stream().filter(p -> matches(p.getArtist(), artist)) 
				.filter(p -> matchesPeriod(p.getPeriod(), periodStr)) 
				.filter(p -> matches(p.getTechnique(), technique)) 
				.filter(p -> matches(p.getMaterial(), material)) 
				.filter(p -> matchesPrice(p.getPrice(), minPrice, maxPrice))
				.collect(Collectors.toList());
	}
	
	private boolean matchesPrice(BigDecimal paintPrice, BigDecimal min, BigDecimal max) {
        if (paintPrice == null) return false;
        boolean afterMin = (min == null) || (paintPrice.compareTo(min) >= 0);
        boolean beforeMax = (max == null) || (paintPrice.compareTo(max) <= 0);
        return afterMin && beforeMax;
    }

	private boolean matches(String dbValue, String filterValue) {
		if (filterValue == null || filterValue.trim().isEmpty()) {
			return true;
		}
		if (dbValue == null)
			return false;
		return dbValue.toLowerCase().contains(filterValue.trim().toLowerCase());
	}

	private boolean matchesPeriod(int dbValue, String filterValue) {
		if (filterValue == null || filterValue.trim().isEmpty()) {
			return true;
		}
		try {
			int year = Integer.parseInt(filterValue.trim());
			return dbValue == year;
		} catch (NumberFormatException e) {
			return true;
		}
		
	}
	
	public Page<Painting> findPaginated(int pageNo, int pageSize) {
	    Pageable pageable = PageRequest.of(pageNo, pageSize);
	    return repository.findAll(pageable);
	}
}
