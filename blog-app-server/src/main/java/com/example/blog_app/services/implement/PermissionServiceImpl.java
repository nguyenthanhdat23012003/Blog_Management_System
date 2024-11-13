package com.example.blog_app.services.implement;

import com.example.blog_app.exceptions.DuplicateResourceException;
import com.example.blog_app.exceptions.ImmutableResourceException;
import com.example.blog_app.exceptions.ResourceNotFoundException;
import com.example.blog_app.models.dtos.PermissionDto;
import com.example.blog_app.models.entities.Permission;
import com.example.blog_app.repositories.PermissionRepository;
import com.example.blog_app.services.PermissionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    public PermissionServiceImpl(PermissionRepository permissionRepository, ModelMapper modelMapper) {
        this.permissionRepository = permissionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PermissionDto createPermission(PermissionDto permissionDto) {
        if (permissionRepository.findByName(permissionDto.getName()).isPresent()) {
            throw new DuplicateResourceException("Permission already exists: " + permissionDto.getName());
        }

        Permission permission = this.dtoToPermission(permissionDto);
        Permission savedPermission = permissionRepository.save(permission);
        return this.permissionToDto(savedPermission);
    }

    @Override
    public PermissionDto updatePermission(PermissionDto permissionDto, String permissionName) {
        if(permissionRepository.findByName(permissionName).isEmpty()){
            throw new ResourceNotFoundException("Permission does not exist: " + permissionName);
        }

        if(permissionRepository.findByName(permissionName).get().isImmutable()){
            throw new ImmutableResourceException("Can not modify ADMIN and USER permission: " + permissionName);
        }

        Permission permission = permissionRepository.findByName(permissionName).get();
        permission.setName(permissionDto.getName());
        Permission savedPermission = permissionRepository.save(permission);
        return this.permissionToDto(savedPermission);
    }

    @Override
    public List<PermissionDto> getAllPermissions() {
        return this.permissionRepository.findAll()
                .stream()
                .map(this::permissionToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePermission(String permissionName) {
        if(permissionRepository.findByName(permissionName).isEmpty()){
            throw new ResourceNotFoundException("Permission does not exist: " + permissionName);
        }

        if(permissionRepository.findByName(permissionName).get().isImmutable()){
            throw new ImmutableResourceException("Can not remove ADMIN and USER permission: " + permissionName);
        }

        Permission permission = permissionRepository.findByName(permissionName).get();
        permissionRepository.delete(permission);
    }

    private PermissionDto permissionToDto(Permission permission){
        return modelMapper.map(permission, PermissionDto.class);
    }

    private Permission dtoToPermission(PermissionDto permissionDto){
        return modelMapper.map(permissionDto, Permission.class);
    }
}
