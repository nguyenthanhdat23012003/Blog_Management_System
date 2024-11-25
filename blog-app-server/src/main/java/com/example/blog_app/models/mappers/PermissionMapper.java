package com.example.blog_app.models.mappers;

import com.example.blog_app.models.dtos.PermissionRequestDto;
import com.example.blog_app.models.dtos.PermissionResponseDto;
import com.example.blog_app.models.entities.Permission;
import org.mapstruct.Mapper;

/**
 * Mapper interface for transforming {@link Permission} entities to DTOs and vice versa.
 *
 * <p>This interface uses MapStruct for mapping between entity and DTO layers,
 * providing a clean and efficient way to handle object transformation.</p>
 *
 * @see Permission
 * @see PermissionRequestDto
 * @see PermissionResponseDto
 */
@Mapper(componentModel = "spring")
public interface PermissionMapper {

    /**
     * Maps a {@link Permission} entity to a {@link PermissionResponseDto}.
     *
     * @param permission the permission entity to map
     * @return the mapped response DTO
     */
    PermissionResponseDto toResponseDto(Permission permission);

    /**
     * Maps a {@link PermissionRequestDto} to a {@link Permission} entity.
     *
     * @param dto the permission request DTO to map
     * @return the mapped permission entity
     */
    Permission toEntity(PermissionRequestDto dto);
}
