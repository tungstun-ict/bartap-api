package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.CategoryService;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.presentation.dto.converter.CategoryConverter;
import com.tungstun.barapi.presentation.dto.request.CategoryRequest;
import com.tungstun.barapi.presentation.dto.response.CategoryResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/bars/{barId}/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryConverter converter;

    public CategoryController(CategoryService categoryService, CategoryConverter converter) {
        this.categoryService = categoryService;
        this.converter = converter;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Finds all categories of bar",
            notes = "Provide id of bar to look up all categories that are linked to the bar",
            response = CategoryResponse.class
    )
    public ResponseEntity<List<CategoryResponse>> getCategoriesOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve categories from") @PathVariable("barId") Long barId,
            @ApiParam(value = "(Optional) Product type of categories you want to retrieve") @RequestParam(value = "productType", required = false) String productType
    ) throws NotFoundException {
        List<Category> categories = this.categoryService.getCategoriesOfBar(barId, productType);
        return new ResponseEntity<>(converter.convertAll(categories), HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Finds category of bar",
            notes = "Provide id of bar and category to look up the specific category from the bar",
            response = CategoryResponse.class
    )
    public ResponseEntity<CategoryResponse> getCategoryOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve the category from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the category you want to retrieve") @PathVariable("categoryId") Long categoryId
    ) throws NotFoundException {
        Category category = this.categoryService.getCategoryOfBar(barId, categoryId);
        return new ResponseEntity<>(converter.convert(category), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Creates new category for bar",
            notes = "Provide id of bar to add new category with information from the request body to the bar",
            response = CategoryResponse.class
    )
    public ResponseEntity<CategoryResponse> addCategoryToBar(
            @ApiParam(value = "ID value for the bar you want to create a new category for") @PathVariable("barId") Long barId,
            @Valid @RequestBody CategoryRequest categoryRequest
    ) throws NotFoundException {
        Category category = this.categoryService.addCategoryToBar(barId, categoryRequest);
        return new ResponseEntity<>(converter.convert(category), HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Updates the category of bar",
            notes = "Provide id of bar to update the category with information from the request body",
            response = CategoryResponse.class
    )
    public ResponseEntity<CategoryResponse> updateCategoryOfBar(
            @ApiParam(value = "ID value for the bar you want to update the category from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the category you want to update") @PathVariable("categoryId") Long categoryId,
            @Valid @RequestBody CategoryRequest categoryRequest
    ) throws NotFoundException {
        Category category = this.categoryService.updateCategoryOfBar(barId, categoryId, categoryRequest);
        return new ResponseEntity<>(converter.convert(category), HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Deletes the category of bar",
            notes = "Provide id of bar to delete the category of bar",
            response = CategoryResponse.class
    )
    public ResponseEntity<Void> deleteCategoryOfBar(
            @ApiParam(value = "ID value for the bar you want to delete the category from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the category you want to delete") @PathVariable("categoryId") Long categoryId
    ) throws NotFoundException {
        this.categoryService.deleteCategoryFromBar(barId, categoryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
