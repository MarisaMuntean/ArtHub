package com.example.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.Painting;
import com.example.service.PaintingDaoService;
import com.example.exception.PaintingNotFoundException;

@Controller
public class PaintingController {
	@Autowired
	PaintingDaoService service;
	
	@GetMapping("/")
    public String showHomePage() {
        return "redirect:/paintings";
    }
	
	@GetMapping("/paintings")
	public String getPaintings(@RequestParam(defaultValue = "0") int page, Model model) {
		int pageSize = 6;
		Page<Painting> pageResult = service.findPaginated(page, pageSize);
		
		model.addAttribute("listPaintings", pageResult);
		
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages",pageResult.getTotalPages());
		model.addAttribute("totalItems",pageResult.getTotalElements());
		model.addAttribute("str","List of available paintings: ");
		
		//add the slider data
		addSliderDataToModel(model);

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
	public String savePainting(@ModelAttribute Painting painting, 
			@RequestParam("imageFile") MultipartFile imageFile,
			RedirectAttributes redirectAttributes) {
		
		if(!imageFile.isEmpty())
		{
			try {
				String uploadDirectory = System.getProperty("user.dir") + "/uploads";
				String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
				
				if(fileName.contains("..")) {
					throw new RuntimeException("Filename contains invalid path sequence " + fileName);
				}
				
				if(!fileName.endsWith(".jpg") && !fileName.endsWith(".png") && !fileName.endsWith(".jpeg"))
				{
					redirectAttributes.addFlashAttribute("errorMessage","Only JPG/PNG/JPEG images are allowed.");
				}
				
				Path uploadPath = Paths.get(uploadDirectory);
				if(!Files.exists(uploadPath))
				{
					Files.createDirectories(uploadPath);
				}
				
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(imageFile.getInputStream(),filePath,StandardCopyOption.REPLACE_EXISTING);
				painting.setImageUrl("/uploads/" + fileName);
			}catch(IOException e)
			{
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("errorMessage","Error uploading image: " + e.getMessage());
				return "redirect:/paintings";
			}
		}
	service.savePainting(painting);
	String mesaj = "You successfully added the painting named " + painting.getTitle();
	redirectAttributes.addFlashAttribute("successMessage", mesaj);
	return "redirect:/paintings";
	}
	
	@PostMapping("/paintings/delete/{id}") 
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
	
	//access in the client to the extreme points
	private void addSliderDataToModel(Model model) {
        model.addAttribute("globalMinPrice", service.getMinPrice());
        model.addAttribute("globalMaxPrice", service.getMaxPrice());
    }
	
	
	@PostMapping("/paintings/filter-advanced")
	public String filterAdvanced(
            @RequestParam(required = false) String artist,
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String technique,
            @RequestParam(required = false) String material,
            @RequestParam(required = false) BigDecimal minPrice, 
            @RequestParam(required = false) BigDecimal maxPrice,
            Model model) {

		List<Painting> result = service.filterAdvanced(artist, period, technique, material, minPrice, maxPrice);
		
        model.addAttribute("listPaintings", result);
        model.addAttribute("str", "Filtered results:");
        
        addSliderDataToModel(model);
        
        //new selected values for the price filter
        model.addAttribute("selectedMinPrice", minPrice);
        model.addAttribute("selectedMaxPrice", maxPrice);
        
        return "paintings";
    }
	
}
