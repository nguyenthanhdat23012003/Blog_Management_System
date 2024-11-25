package com.example.blog_app.models.mappers;

import com.example.blog_app.models.dtos.SeriesRequestDto;
import com.example.blog_app.models.dtos.SeriesResponseDto;
import com.example.blog_app.models.entities.Series;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper interface for transforming {@link Series} entities to DTOs and vice versa.
 *
 * <p>This interface uses MapStruct for mapping between entity and DTO layers,
 * providing a clean and efficient way to handle object transformation.</p>
 *
 * @see Series
 * @see SeriesRequestDto
 * @see SeriesResponseDto
 */
@Mapper(componentModel = "spring")
public interface SeriesMapper {

    /**
     * Converts a {@link Series} entity to a {@link SeriesResponseDto}.
     *
     * @param series the series entity to convert
     * @return the converted response DTO
     */
    @Mapping(source = "user.id", target = "authorId")
    SeriesResponseDto toResponseDto(Series series);

    /**
     * Converts a {@link SeriesRequestDto} to a {@link Series} entity.
     *
     * @param seriesRequestDto the request DTO to convert
     * @return the converted series entity
     */
    Series toEntity(SeriesRequestDto seriesRequestDto);

    /**
     * Updates an existing {@link Series} entity with values from a {@link SeriesRequestDto}.
     *
     * @param seriesRequestDto the DTO containing updated series details
     * @param series           the existing series entity to update
     */
    @Mapping(target = "id", ignore = true) // Prevent ID from being overwritten during updates
    @Mapping(target = "user", ignore = true) // User will not be updated here
    void updateEntityFromDto(SeriesRequestDto seriesRequestDto, @MappingTarget Series series);
}
