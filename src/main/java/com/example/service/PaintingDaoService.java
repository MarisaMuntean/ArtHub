package com.example.service;

import java.util.List;
import java.util.Optional;

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
		if(repository.existsById(id))
		{
			repository.deleteById(id);
			return true;
		}

	return false;
	}

	
}
