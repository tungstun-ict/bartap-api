package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.CategoryService;
import com.tungstun.barapi.domain.Category;
import com.tungstun.barapi.presentation.dto.request.CategoryRequest;
import com.tungstun.barapi.presentation.dto.response.CategoryResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
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
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Finds all categories of bar",
            notes = "Provide id of bar to look up all categories that are linked to the bar",
            response = CategoryResponse.class,
            responseContainer = "List"
    )
    public ResponseEntity<List<CategoryResponse>> getCategoriesOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve categories from") @PathVariable("barId") Long barId,
            @ApiParam(value = "(Optional) Product type of categories you want to retrieve") @RequestParam(value = "productType", required = false) String productType
    ) throws NotFoundException {
        List<Category> categories = this.CATEGORY_SERVICE.getCategoriesOfBar(barId, productType);
        List<CategoryResponse> categoryResponses = RESPONSE_MAPPER.convertList(categories, CategoryResponse.class);
        return new ResponseEntity<>(categoryResponses, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Finds category of bar",
            notes = "Provide id of bar and category to look up the specific category from the bar",
            response = CategoryResponse.class
    )
    public ResponseEntity<CategoryResponse> getCategoryOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve the category from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the category you want to retrieve") @PathVariable("categoryId") Long categoryId
    ) throws NotFoundException {
        Category category = this.CATEGORY_SERVICE.getCategoryOfBar(barId, categoryId);
        return new ResponseEntity<>(convertToCategoryResult(category), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Creates new category for bar",
            notes = "Provide id of bar to add new category with information from the request body to the bar",
            response = CategoryResponse.class
    )
    public ResponseEntity<CategoryResponse> addCategoryToBar(
            @ApiParam(value = "ID value for the bar you want to create a new category for") @PathVariable("barId") Long barId,
            @Valid @RequestBody CategoryRequest categoryRequest
    ) throws NotFoundException {
        Category category = this.CATEGORY_SERVICE.addCategoryToBar(barId, categoryRequest);
        return new ResponseEntity<>(convertToCategoryResult(category), HttpStatus.OK);
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
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
        Category category = this.CATEGORY_SERVICE.updateCategoryOfBar(barId, categoryId, categoryRequest);
        return new ResponseEntity<>(convertToCategoryResult(category), HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Deletes the category of bar",
            notes = "Provide id of bar to delete the category of bar",
            response = CategoryResponse.class
    )
    public ResponseEntity<Void> deleteCategoryOfBar(
            @ApiParam(value = "ID value for the bar you want to delete the category from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the category you want to delete") @PathVariable("categoryId") Long categoryId
    ) throws NotFoundException {
        this.CATEGORY_SERVICE.deleteCategoryFromBar(barId, categoryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
