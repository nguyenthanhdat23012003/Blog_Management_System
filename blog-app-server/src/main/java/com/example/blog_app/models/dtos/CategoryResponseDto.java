package com.example.blog_app.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for handling category-related responses.
 *
 * <p>This class is used to provide category details in response payloads
 * for operations such as retrieving a single category or a list of categories.</p>
 *
 * <p>It includes all the necessary fields to represent a category
 * and can be extended if additional metadata is required.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * CategoryResponseDto category = new CategoryResponseDto();
 * category.setId(1);
 * category.setTitle("Programming");
 * category.setDescription("Learn about programming languages and techniques.");
 * }
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
public class CategoryResponseDto {

    /**
     * The unique identifier of the category.
     *
     * <p>Generated automatically when the category is created.</p>
     */
    private Integer id;

    /**
     * The title of the category.
     *
     * <p>This field represents the name or label of the category.</p>
     */
    private String title;

    /**
     * The description of the category.
     *
     * <p>Provides additional details or context about the category.</p>
     */
    private String description;
}
