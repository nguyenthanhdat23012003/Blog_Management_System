package com.example.blog_app.services.implement;

import com.example.blog_app.exceptions.ResourceNotFoundException;
import com.example.blog_app.models.dtos.CategoryRequestDto;
import com.example.blog_app.models.dtos.CategoryResponseDto;
import com.example.blog_app.models.entities.Category;
import com.example.blog_app.repositories.CategoryRepository;
import com.example.blog_app.services.CategoryService;
import com.example.blog_app.models.mappers.CategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link CategoryService} interface for managing categories.
 *
 * <p>This service provides the business logic for category-related operations such as:</p>
 * <ul>
 *   <li>Creating a new category.</li>
 *   <li>Updating category details.</li>
 *   <li>Retrieving category information.</li>
 *   <li>Deleting a category.</li>
 * </ul>
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    /**
     * Constructs the CategoryServiceImpl with required dependencies.
     *
     * @param categoryRepository the repository for managing categories
     * @param categoryMapper     the mapper for converting between entities and DTOs
     */
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    /**
     * Creates a new category.
     *
     * @param categoryDto the DTO containing category details for creation
     * @return the created category's details as a response DTO
     */
    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponseDto(savedCategory);
    }

    /**
     * Updates an existing category's information.
     *
     * @param categoryDto the DTO containing updated category details
     * @param categoryId  the ID of the category to update
     * @return the updated category's details as a response DTO
     * @throws ResourceNotFoundException if the category does not exist
     */
    @Override
    public CategoryResponseDto updateCategoryById(CategoryRequestDto categoryDto, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));

        categoryMapper.updateEntityFromDto(categoryDto, category);
        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toResponseDto(updatedCategory);
    }

    /**
     * Retrieves all categories.
     *
     * @return a list of category response DTOs representing all categories
     */
    @Override
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponseDto)
                .toList();
    }

    /**
     * Retrieves the details of a specific category.
     *
     * @param categoryId the ID of the category to retrieve
     * @return the category's details as a response DTO
     * @throws ResourceNotFoundException if the category does not exist
     */
    @Override
    public CategoryResponseDto getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));

        return categoryMapper.toResponseDto(category);
    }

    /**
     * Deletes a category by its ID.
     *
     * @param categoryId the ID of the category to delete
     * @throws ResourceNotFoundException if the category does not exist
     */
    @Override
    public void deleteCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));

        categoryRepository.delete(category);
    }
}
