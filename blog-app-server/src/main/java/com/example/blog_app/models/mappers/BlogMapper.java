package com.example.blog_app.models.mappers;

import com.example.blog_app.common.util.JsonConverter;
import com.example.blog_app.models.dtos.blog.BlogRequestDto;
import com.example.blog_app.models.dtos.blog.BlogResponseDto;
import com.example.blog_app.models.entities.Blog;
import com.example.blog_app.models.entities.Category;
import org.mapstruct.*;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper interface for transforming {@link Blog} entities to DTOs and vice versa.
 *
 * <p>This interface uses MapStruct for mapping between entity and DTO layers,
 * providing a clean and efficient way to handle object transformation.</p>
 *
 * <p>Specific transformation logic includes:</p>
 * <ul>
 *     <li>Mapping between {@code Category} entities and their IDs.</li>
 *     <li>Converting {@code content} stored as a JSON string in the database to a {@link Map} and vice versa.</li>
 * </ul>
 *
 * <p>MapStruct annotations like {@link Named} are used to handle custom mappings.</p>
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
     * <p>Converts relational fields such as {@code user}, {@code categories}, and {@code series}
     * into their respective ID representations in the DTO.</p>
     *
     * <p>Additionally, transforms the {@code content} field from JSON format to a {@link Map}.</p>
     *
     * @param blog the blog entity to map
     * @return the mapped {@link BlogResponseDto}
     */
    @Mapping(target = "authorId", source = "user.id")
    @Mapping(target = "seriesId", source = "series.id")
    @Mapping(target = "categoryIds", source = "categories", qualifiedByName = "categoriesToCategoryIds")
    @Mapping(target = "content", source = "content", qualifiedByName = "jsonToMap")
    BlogResponseDto toResponseDto(Blog blog);

    /**
     * Maps a {@link BlogRequestDto} to a {@link Blog} entity.
     *
     * <p>Ignores relational fields such as {@code user}, {@code series}, and {@code categories},
     * as they will be resolved in the service layer.</p>
     *
     * <p>Converts the {@code content} field from a {@link Map} to JSON format for storage in the database.</p>
     *
     * @param dto the blog request DTO to map
     * @return the mapped {@link Blog} entity
     */
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "series", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "content", source = "content", qualifiedByName = "mapToJson")
    Blog toEntity(BlogRequestDto dto);

    /**
     * Updates an existing {@link Blog} entity with values from a {@link BlogRequestDto}.
     *
     * <p>Updates fields in the {@link Blog} entity while ignoring null values.</p>
     *
     * <p>The {@code content} field is transformed from a {@link Map} to JSON format
     * for database storage.</p>
     *
     * @param blogDto the DTO containing updated blog data
     * @param blog    the existing {@link Blog} entity to update
     */
    @Mapping(target = "content", source = "content", qualifiedByName = "mapToJson")
    void updateBlogFromDto(BlogRequestDto blogDto, @MappingTarget Blog blog);

    /**
     * Custom mapping to convert a set of {@link Category} entities to a set of their IDs.
     *
     * <p>Used in the transformation of {@link Blog} entities to DTOs.</p>
     *
     * @param categories the set of {@link Category} entities
     * @return a set of IDs corresponding to the categories
     */
    @Named("categoriesToCategoryIds")
    default Set<Long> mapCategoriesToCategoryIds(Set<Category> categories) {
        return categories.stream().map(Category::getId).collect(Collectors.toSet());
    }

    /**
     * Custom mapping to convert a JSON string to a {@link Map}.
     *
     * <p>Uses the {@link JsonConverter} utility to handle the transformation.</p>
     *
     * @param content the JSON string to convert
     * @return the resulting {@link Map}, or {@code null} if the input is {@code null}
     */
    @Named("jsonToMap")
    default Map<String, Object> jsonToMap(String content) {
        return JsonConverter.jsonToMap(content);
    }

    /**
     * Custom mapping to convert a {@link Map} to a JSON string.
     *
     * <p>Uses the {@link JsonConverter} utility to handle the transformation.</p>
     *
     * @param content the {@link Map} to convert
     * @return the resulting JSON string, or {@code null} if the input is {@code null}
     */
    @Named("mapToJson")
    default String mapToJson(Map<String, Object> content) {
        return JsonConverter.mapToJson(content);
    }
}
