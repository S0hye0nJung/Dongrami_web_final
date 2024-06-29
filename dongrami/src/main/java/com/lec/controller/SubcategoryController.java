package com.lec.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lec.entity.Subcategory;
import com.lec.service.SubcategoryService;

import java.util.List;

@Controller
public class SubcategoryController {
	
    @Autowired
    private SubcategoryService subcategoryService;

    @GetMapping("/topic") // 변경된 URL 패스
    public String getSubcategories(Model model) {
        List<Subcategory> subcategories = subcategoryService.getAllSubcategories();
        model.addAttribute("subcategories", subcategories);
        
        return "topicpage";
    }

    @GetMapping("/ranking") // 추가된 URL 패스
    public String getTop5Subcategories(Model model) {
        List<Subcategory> top5Subcategories = subcategoryService.getTop5SubcategoriesByCount();
        
        // Ensure we have at least 5 elements to avoid IndexOutOfBoundsException
        if (top5Subcategories.size() >= 5) {
            model.addAttribute("ranking1", top5Subcategories.get(0).getBubble_slack_name());
            model.addAttribute("ranking2", top5Subcategories.get(1).getBubble_slack_name());
            model.addAttribute("ranking3", top5Subcategories.get(2).getBubble_slack_name());
            model.addAttribute("ranking4", top5Subcategories.get(3).getBubble_slack_name());
            model.addAttribute("ranking5", top5Subcategories.get(4).getBubble_slack_name());
        } else {
            // Handle case where less than 5 elements are returned
            model.addAttribute("ranking1", "");
            model.addAttribute("ranking2", "");
            model.addAttribute("ranking3", "");
            model.addAttribute("ranking4", "");
            model.addAttribute("ranking5", "");
        }
        
        return "rankingpage";
    }

    @PostMapping("/updateClickCount")
    @ResponseBody
    public String updateClickCount(@RequestBody Map<String, String> payload) {
        int subcategoryId = Integer.parseInt(payload.get("subcategoryId"));
        int count = Integer.parseInt(payload.get("count"));

        subcategoryService.updateClickCount(subcategoryId, count);
        return "success";
    }
}