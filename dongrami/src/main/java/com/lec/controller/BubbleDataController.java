package com.lec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lec.entity.Subcategory;
import com.lec.service.SubcategoryService;

@RestController
@RequestMapping("/getBubbleData")
public class BubbleDataController {
    private final SubcategoryService subcategoryService;

    @Autowired
    public BubbleDataController(SubcategoryService subcategoryService) {
        this.subcategoryService = subcategoryService;
    }

    @GetMapping
    public List<Subcategory> getBubbleData() {
        return subcategoryService.getTop5SubcategoriesByCount();
    }
}