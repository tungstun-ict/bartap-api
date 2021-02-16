package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.CategoryService;
import com.tungstun.barapi.domain.Category;
import com.tungstun.barapi.presentation.dto.request.CategoryRequest;
import com.tungstun.barapi.presentation.dto.response.CategoryResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<List<CategoryResponse>> getCategoriesOfBar(
            @PathVariable("barId") Long barId,
            @RequestParam(value = "productType", required = false) String productType
    ) throws NotFoundException {
        List<Category> categories = this.CATEGORY_SERVICE.getCategoriesOfBar(barId, productType);
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

    @PostMapping
    public ResponseEntity<CategoryResponse> addCategoryToBar(
            @PathVariable("barId") Long barId,
            @Valid @RequestBody CategoryRequest categoryRequest
    ) throws NotFoundException {
        Category category = this.CATEGORY_SERVICE.addCategoryToBar(barId, categoryRequest);
        return new ResponseEntity<>(convertToCategoryResult(category), HttpStatus.OK);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategoryOfBar(
            @PathVariable("barId") Long barId,
            @PathVariable("categoryId") Long categoryId,
            @Valid @RequestBody CategoryRequest categoryRequest
    ) throws NotFoundException {
        Category category = this.CATEGORY_SERVICE.updateCategoryOfBar(barId, categoryId, categoryRequest);
        return new ResponseEntity<>(convertToCategoryResult(category), HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategoryOfBar(
            @PathVariable("barId") Long barId,
            @PathVariable("categoryId") Long categoryId
    ) throws NotFoundException {
        this.CATEGORY_SERVICE.deleteCategoryFromBar(barId, categoryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
