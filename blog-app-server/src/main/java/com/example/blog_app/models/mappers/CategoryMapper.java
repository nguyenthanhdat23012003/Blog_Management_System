package com.example.blog_app.models.mappers;

import com.example.blog_app.models.dtos.category.CategoryRequestDto;
import com.example.blog_app.models.dtos.category.CategoryResponseDto;
import com.example.blog_app.models.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper interface for transforming {@link Category} entities to DTOs and vice versa.
 *
 * <p>This interface uses MapStruct for mapping between entity and DTO layers,
 * providing a clean and efficient way to handle object transformation.</p>
 *
 * @see Category
 * @see CategoryRequestDto
 * @see CategoryResponseDto
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {

    /**
     * Converts a {@link Category} entity to a {@link CategoryResponseDto}.
     *
     * @param category the category entity to convert
     * @return the converted response DTO
     */
    CategoryResponseDto toResponseDto(Category category);

    /**
     * Converts a {@link CategoryRequestDto} to a {@link Category} entity.
     *
     * @param categoryRequestDto the request DTO to convert
     * @return the converted category entity
     */
    Category toEntity(CategoryRequestDto categoryRequestDto);

    /**
     * Updates an existing {@link Category} entity with values from a {@link CategoryRequestDto}.
     *
     * <p>Any null values in the DTO will not override existing values in the entity.</p>
     *
     * @param categoryRequestDto the DTO containing updated category details
     * @param category           the existing category entity to update
     */
    @Mapping(target = "id", ignore = true) // Prevent ID from being overwritten during updates
    void updateEntityFromDto(CategoryRequestDto categoryRequestDto, @MappingTarget Category category);
}
