package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.Painting;
import com.example.service.PaintingDaoService;
import com.example.exception.PaintingNotFoundException;

@Controller
public class PaintingController {
	@Autowired
	PaintingDaoService service;
	
	@GetMapping("/paintings")
	public String getPaintings(Model model) {
	String s="List of available paintings: ";
	model.addAttribute("str",s);
	model.addAttribute("listPaintings",service.findAllPaintings());
	return "paintings";
	}
	
	@PostMapping("/paintings/find")
	public String findById(@RequestParam(required = false) Long id, Model model) {
		if (id == null) {
	        
	        throw new PaintingNotFoundException("You didn't enter an Id.");
	    }
	Painting painting = service.findById(id);
	if (painting == null)
	throw new PaintingNotFoundException("Painting with id " + id + " was not found.");
	model.addAttribute("str","Painting found:");
	model.addAttribute("listPaintings",List.of(painting));
	return "paintings";
	}
	
	@PostMapping("/paintings/add")
	public String savePainting(@ModelAttribute Painting painting, RedirectAttributes redirectAttributes) {
	service.savePainting(painting);
	String mesaj = "You successfully added the painting named " + painting.getTitle();
	redirectAttributes.addFlashAttribute("successMessage", mesaj);
	return "redirect:/paintings";
	}
	
	@GetMapping("/paintings/delete/{id}") 
	public String deletePainting(@PathVariable Long id, RedirectAttributes redirectAttributes) {
	    Painting painting = service.findById(id);
	    
	    if (painting != null) {
	        String title = painting.getTitle();
	        service.deleteById(id);
	        redirectAttributes.addFlashAttribute("successMessage", "You deleted the painting named " + title);
	    }
	    
	    return "redirect:/paintings";
	}

	@PostMapping("/paintings/update")
	public String updatePainting(@ModelAttribute Painting painting, RedirectAttributes redirectAttributes) {
	    service.savePainting(painting);
	    
	    redirectAttributes.addFlashAttribute("successMessage", "Painting '" + painting.getTitle() + "' was successfully updated!");
	    return "redirect:/paintings";
	}
}
