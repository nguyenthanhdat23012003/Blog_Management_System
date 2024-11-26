package com.example.blog_app.controllers;

import com.example.blog_app.common.validation.CreateValidationGroup;
import com.example.blog_app.common.validation.UpdateValidationGroup;
import com.example.blog_app.models.dtos.category.CategoryRequestDto;
import com.example.blog_app.models.dtos.category.CategoryResponseDto;
import com.example.blog_app.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing categories in the application.
 *
 * <p>This controller provides endpoints for category creation, retrieval,
 * updating, and deletion. Categories are used to organize and group
 * blogs effectively within the system.</p>
 *
 * <p>Example endpoints:</p>
 * <ul>
 *   <li>POST /api/categories - Create a new category</li>
 *   <li>PUT /api/categories/{categoryId} - Update an existing category</li>
 *   <li>GET /api/categories/{categoryId} - Retrieve category details by ID</li>
 *   <li>GET /api/categories - Retrieve all categories</li>
 *   <li>DELETE /api/categories/{categoryId} - Delete a category</li>
 * </ul>
 *
 * @see CategoryService
 * @see CategoryRequestDto
 * @see CategoryResponseDto
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Constructs a new {@link CategoryController} with the given {@link CategoryService}.
     *
     * @param categoryService the service to handle category-related business logic
     */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Creates a new category.
     *
     * @param categoryDto the DTO containing category details
     * @return the created category as a {@link CategoryResponseDto}
     */
    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(
            @Validated(CreateValidationGroup.class) @RequestBody CategoryRequestDto categoryDto) {
        CategoryResponseDto createdCategory = categoryService.createCategory(categoryDto);
        return ResponseEntity.status(201).body(createdCategory);
    }

    /**
     * Updates an existing category.
     *
     * @param categoryDto the DTO containing updated category details
     * @param categoryId  the ID of the category to update
     * @return the updated category as a {@link CategoryResponseDto}
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @Validated(UpdateValidationGroup.class) @RequestBody CategoryRequestDto categoryDto,
            @PathVariable Long categoryId) {
        CategoryResponseDto updatedCategory = categoryService.updateCategoryById(categoryDto, categoryId);
        return ResponseEntity.status(200).body(updatedCategory);
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param categoryId the ID of the category to retrieve
     * @return the category details as a {@link CategoryResponseDto}
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> getCategory(@PathVariable Long categoryId) {
        CategoryResponseDto category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.status(200).body(category);
    }

    /**
     * Retrieves all categories.
     *
     * @return a list of all categories as {@link CategoryResponseDto}
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        return ResponseEntity.status(200).body(categories);
    }

    /**
     * Deletes a category by its ID.
     *
     * @param categoryId the ID of the category to delete
     * @return a confirmation message indicating the deletion
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return ResponseEntity.ok("Category deleted successfully");
    }
}
