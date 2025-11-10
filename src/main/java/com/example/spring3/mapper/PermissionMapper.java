package com.example.spring3.mapper;


import com.example.spring3.dto.request.PermissionRequest;
import com.example.spring3.dto.request.UserCreateRequest;
import com.example.spring3.dto.request.UserUpdateRequest;
import com.example.spring3.dto.response.PermissionResponse;
import com.example.spring3.dto.response.UserResponse;
import com.example.spring3.entity.Permission;
import com.example.spring3.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}

