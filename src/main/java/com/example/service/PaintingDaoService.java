package com.example.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

	public List<Painting> filterAdvanced(String artist, String periodStr, String technique, String material) {

		List<Painting> allPaintings = repository.findAll();

		return allPaintings.stream().filter(p -> matches(p.getArtist(), artist)) // Verificăm Artist
				.filter(p -> matchesPeriod(p.getPeriod(), periodStr)) // Verificăm Anul
				.filter(p -> matches(p.getTechnique(), technique)) // Verificăm Tehnica
				.filter(p -> matches(p.getMaterial(), material)) // Verificăm Material
				.collect(Collectors.toList());
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
}
