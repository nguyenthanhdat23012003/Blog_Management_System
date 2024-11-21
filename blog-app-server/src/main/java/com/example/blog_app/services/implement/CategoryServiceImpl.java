package com.example.blog_app.services.implement;

import com.example.blog_app.exceptions.ResourceNotFoundException;
import com.example.blog_app.models.dtos.CategoryRequestDto;
import com.example.blog_app.models.dtos.CategoryResponseDto;
import com.example.blog_app.models.entities.Category;
import com.example.blog_app.repositories.CategoryRepository;
import com.example.blog_app.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    private final ModelMapper modelMapper;

    /**
     * Constructs the CategoryServiceImpl with required dependencies.
     *
     * @param categoryRepository the repository for managing categories
     * @param modelMapper        the model mapper for DTO and entity conversion
     */
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Creates a new category.
     *
     * @param categoryDto the DTO containing category details for creation
     * @return the created category's details as a response DTO
     */
    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryDto) {
        Category category = this.dtoToCategory(categoryDto);
        category.setTitle(categoryDto.getTitle());
        if (categoryDto.getDescription() != null && !categoryDto.getDescription().isEmpty()) {
            category.setDescription(categoryDto.getDescription());
        }
        Category savedCategory = this.categoryRepository.save(category);

        return this.categoryToDto(savedCategory);
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
    public CategoryResponseDto updateCategory(CategoryRequestDto categoryDto, Long categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));

        if (categoryDto.getDescription() != null && !categoryDto.getDescription().isEmpty()) {
            category.setDescription(categoryDto.getDescription());
        }

        if (categoryDto.getTitle() != null && !categoryDto.getTitle().isEmpty()) {
            category.setTitle(categoryDto.getTitle());
        }

        Category savedCategory = this.categoryRepository.save(category);
        return this.categoryToDto(savedCategory);
    }

    /**
     * Retrieves all categories.
     *
     * @return a list of category response DTOs representing all categories
     */
    @Override
    public List<CategoryResponseDto> getAllCategories() {
        return this.categoryRepository.findAll()
                .stream()
                .map(this::categoryToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the details of a specific category.
     *
     * @param categoryId the ID of the category to retrieve
     * @return the category's details as a response DTO
     * @throws ResourceNotFoundException if the category does not exist
     */
    @Override
    public CategoryResponseDto getCategory(Long categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));

        return this.categoryToDto(category);
    }

    /**
     * Deletes a category by its ID.
     *
     * @param categoryId the ID of the category to delete
     * @throws ResourceNotFoundException if the category does not exist
     */
    @Override
    public void deleteCategory(Long categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));

        this.categoryRepository.delete(category);
    }

    /**
     * Converts a {@link CategoryRequestDto} to a {@link Category} entity.
     *
     * @param categoryDto the DTO to convert
     * @return the converted entity
     */
    private Category dtoToCategory(CategoryRequestDto categoryDto) {
        return modelMapper.map(categoryDto, Category.class);
    }

    /**
     * Converts a {@link Category} entity to a {@link CategoryResponseDto}.
     *
     * @param category the category entity to convert
     * @return the converted DTO
     */
    private CategoryResponseDto categoryToDto(Category category) {
        return modelMapper.map(category, CategoryResponseDto.class);
    }
}
