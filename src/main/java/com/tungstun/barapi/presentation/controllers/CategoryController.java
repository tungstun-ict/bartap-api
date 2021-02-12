package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.CategoryService;
import com.tungstun.barapi.domain.Category;
import com.tungstun.barapi.presentation.dto.response.CategoryResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("bars/{barId}/categories")
public class CategoryController {
    private final CategoryService CATEGORY_SERVICE;
    private final ResponseMapper RESPONSE_MAPPER;

    public CategoryController(CategoryService categoryService, ResponseMapper responseMapper) {
        this.CATEGORY_SERVICE = categoryService;
        this.RESPONSE_MAPPER = responseMapper;
    }

    private CategoryResponse convertToCategoryResult(Category category){
        return RESPONSE_MAPPER.convert(category, CategoryResponse.class);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategoriesOfBar(
            @PathVariable("barId") Long barId
    ) throws NotFoundException {
        List<Category> categories = this.CATEGORY_SERVICE.getAllCategoriesOfBar(barId);
        List<CategoryResponse> categoryResponses = RESPONSE_MAPPER.convertList(categories, CategoryResponse.class);
        return new ResponseEntity<>(categoryResponses, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryOfBar(
            @PathVariable("barId") Long barId,
            @PathVariable("categoryId") Long categoryId
    ) throws NotFoundException {
        Category category = this.CATEGORY_SERVICE.getCategoryOfBar(barId, categoryId);
        return new ResponseEntity<>(convertToCategoryResult(category), HttpStatus.OK);
    }
}
