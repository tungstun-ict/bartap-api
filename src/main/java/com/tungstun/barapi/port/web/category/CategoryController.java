package com.tungstun.barapi.port.web.category;

import com.tungstun.barapi.application.category.CategoryCommandHandler;
import com.tungstun.barapi.application.category.CategoryQueryHandler;
import com.tungstun.barapi.application.category.command.CreateCategory;
import com.tungstun.barapi.application.category.command.DeleteCategory;
import com.tungstun.barapi.application.category.command.UpdateCategory;
import com.tungstun.barapi.application.category.query.GetCategory;
import com.tungstun.barapi.application.category.query.ListCategoriesOfBar;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.port.web.category.converter.CategoryConverter;
import com.tungstun.barapi.port.web.category.request.CreateCategoryRequest;
import com.tungstun.barapi.port.web.category.request.UpdateCategoryRequest;
import com.tungstun.barapi.port.web.category.response.CategoryResponse;
import com.tungstun.common.response.UuidResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/bars/{barId}/categories")
public class CategoryController {
    private final CategoryQueryHandler categoryQueryHandler;
    private final CategoryCommandHandler categoryCommandHandler;
    private final CategoryConverter converter;

    public CategoryController(CategoryQueryHandler categoryQueryHandler, CategoryCommandHandler categoryCommandHandler, CategoryConverter converter) {
        this.categoryQueryHandler = categoryQueryHandler;
        this.categoryCommandHandler = categoryCommandHandler;
        this.converter = converter;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Finds all categories of bar",
            description = "Find all the categories of the bar with the given id"
    )
    public List<CategoryResponse> getCategoriesOfBar(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId
    ) throws EntityNotFoundException {
        List<Category> categories = categoryQueryHandler.handle(new ListCategoriesOfBar(barId));
        return converter.convertAll(categories);
    }

    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Finds category",
            description = "Find category of bar with given id's"
    )
    public CategoryResponse getCategoryOfBar(
            @Param(value = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Param(value = "Id value of the category") @PathVariable("categoryId") UUID categoryId
    ) throws EntityNotFoundException {
        Category category = categoryQueryHandler.handle(new GetCategory(barId, categoryId));
        return converter.convert(category);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Creates a category",
            description = "Create a new category for a bar with the given information"
    )
    public UuidResponse addCategoryToBar(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Valid @RequestBody CreateCategoryRequest request
    ) throws EntityNotFoundException {
        CreateCategory command = new CreateCategory(barId, request.name());
        return new UuidResponse(categoryCommandHandler.handle(command));
    }

    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Updates a category of bar",
            description = "Update a category of a bar with the given information"
    )
    public UuidResponse updateCategoryOfBar(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id value of the category") @PathVariable("categoryId") UUID categoryId,
            @Valid @RequestBody UpdateCategoryRequest request
    ) throws EntityNotFoundException {
        UpdateCategory command = new UpdateCategory(barId, categoryId, request.name());
        return new UuidResponse(categoryCommandHandler.handle(command));
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Deletes a category",
            description = "Delete a category of a bar with given id's"
    )
    public void deleteCategoryOfBar(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id value of the category") @PathVariable("categoryId") UUID categoryId
    ) throws EntityNotFoundException {
        DeleteCategory command = new DeleteCategory(categoryId);
        categoryCommandHandler.handle(command);
    }
}
