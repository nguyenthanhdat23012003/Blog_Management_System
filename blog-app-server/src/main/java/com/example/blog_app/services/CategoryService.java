package com.example.blog_app.services;

import com.example.blog_app.models.dtos.CategoryRequestDto;
import com.example.blog_app.models.dtos.CategoryResponseDto;

import java.util.List;

/**
 * Service interface for managing categories.
 *
 * <p>This interface defines the contract for operations related to blog categories,
 * including creating, updating, retrieving, and deleting categories.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * CategoryService categoryService = new CategoryServiceImpl();
 * CategoryResponseDto category = categoryService.getCategory(1L);
 * }
 * </pre>
 */
public interface CategoryService {

    /**
     * Creates a new category.
     *
     * @param categoryDto the DTO containing the details of the category to be created
     * @return the created category as a response DTO
     */
    CategoryResponseDto createCategory(CategoryRequestDto categoryDto);

    /**
     * Updates an existing category.
     *
     * @param categoryDto the DTO containing the updated category details
     * @return the updated category as a response DTO
     */
    CategoryResponseDto updateCategory(CategoryRequestDto categoryDto, Long categoryId);

    /**
     * Retrieves all categories.
     *
     * @return a list of category response DTOs representing all categories
     */
    List<CategoryResponseDto> getAllCategories();

    /**
     * Retrieves a specific category by its ID.
     *
     * @param categoryId the ID of the category to retrieve
     * @return the category as a response DTO
     */
    CategoryResponseDto getCategory(Long categoryId);

    /**
     * Deletes a specific category by its ID.
     *
     * @param categoryId the ID of the category to delete
     */
    void deleteCategory(Long categoryId);
}
