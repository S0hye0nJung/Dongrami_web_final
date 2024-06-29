package com.lec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TopicController {
	
	
	@GetMapping("/tarot")
    public String rematch(@RequestParam("subcategory_id") int subcategoryId, Model model) {
        model.addAttribute("subcategoryId", subcategoryId);
        
        if (subcategoryId == 1 || subcategoryId == 2 || subcategoryId == 3 || 
            subcategoryId == 4 || subcategoryId == 5 || subcategoryId == 10) {
            return "tarot";
        } else {
            return "tarot_oneSelect";
        }
    }
}
