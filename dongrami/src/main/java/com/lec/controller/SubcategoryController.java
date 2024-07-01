package com.lec.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lec.entity.Subcategory;
import com.lec.service.SubcategoryService;

@Controller
public class SubcategoryController {
    
    @Autowired
    private SubcategoryService subcategoryService;

    @GetMapping("/topic")
    public String getSubcategories(Model model) {
        List<Subcategory> subcategories = subcategoryService.getAllSubcategories();
        model.addAttribute("subcategories", subcategories);
        return "topicpage";
    }

    @GetMapping("/ranking")
    public String getTop5Subcategories(Model model) {
        List<Subcategory> top5Subcategories = subcategoryService.getTop5SubcategoriesByCount();
        
        model.addAttribute("ranking1", top5Subcategories.size() > 0 ? top5Subcategories.get(0).getBubble_slack_name() : "");
        model.addAttribute("ranking2", top5Subcategories.size() > 1 ? top5Subcategories.get(1).getBubble_slack_name() : "");
        model.addAttribute("ranking3", top5Subcategories.size() > 2 ? top5Subcategories.get(2).getBubble_slack_name() : "");
        model.addAttribute("ranking4", top5Subcategories.size() > 3 ? top5Subcategories.get(3).getBubble_slack_name() : "");
        model.addAttribute("ranking5", top5Subcategories.size() > 4 ? top5Subcategories.get(4).getBubble_slack_name() : "");
        
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
