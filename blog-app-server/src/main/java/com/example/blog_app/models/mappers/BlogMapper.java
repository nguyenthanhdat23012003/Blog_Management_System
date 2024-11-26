package com.example.blog_app.models.mappers;

import com.example.blog_app.models.dtos.BlogRequestDto;
import com.example.blog_app.models.dtos.BlogResponseDto;
import com.example.blog_app.models.entities.Blog;
import com.example.blog_app.models.entities.Category;
import com.example.blog_app.models.entities.Series;
import com.example.blog_app.models.entities.User;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper interface for transforming {@link Blog} entities to DTOs and vice versa.
 *
 * <p>This interface uses MapStruct for mapping between entity and DTO layers,
 * providing a clean and efficient way to handle object transformation.</p>
 *
 * <p>Specific transformation logic, such as mapping between {@code Category} and
 * {@code categoryIds}, is implemented through custom methods annotated with {@link Named}.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * Blog blog = new Blog();
 * BlogResponseDto responseDto = blogMapper.toResponseDto(blog);
 * }
 * </pre>
 *
 * @see Blog
 * @see BlogRequestDto
 * @see BlogResponseDto
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BlogMapper {

    /**
     * Maps a {@link Blog} entity to a {@link BlogResponseDto}.
     *
     * @param blog the blog entity to map
     * @return the mapped response DTO
     */
    @Mapping(target = "authorId", source = "user.id")
    @Mapping(target = "seriesId", source = "series.id")
    @Mapping(target = "categoryIds", source = "categories", qualifiedByName = "categoriesToCategoryIds")
    BlogResponseDto toResponseDto(Blog blog);

    /**
     * Maps a {@link BlogRequestDto} to a {@link Blog} entity.
     *
     * @param dto the blog request DTO to map
     * @return the mapped blog entity
     */
    @Mapping(target = "user", ignore = true) // User is resolved in the service layer
    @Mapping(target = "series", ignore = true) // Series is resolved in the service layer
    @Mapping(target = "categories", ignore = true) // Categories are resolved in the service layer
    Blog toEntity(BlogRequestDto dto);

    /**
     * Updates an existing {@link Blog} entity with values from a {@link BlogRequestDto}.
     *
     * @param blogDto the DTO containing updated blog data
     * @param blog    the existing blog entity to update
     */
    void updateBlogFromDto(BlogRequestDto blogDto, @MappingTarget Blog blog);

    /**
     * Custom mapping to convert a set of {@link Category} entities to a set of category IDs.
     *
     * @param categories the set of categories to map
     * @return a set of category IDs
     */
    @Named("categoriesToCategoryIds")
    default Set<Long> mapCategoriesToCategoryIds(Set<Category> categories) {
        return categories.stream().map(Category::getId).collect(Collectors.toSet());
    }
}
