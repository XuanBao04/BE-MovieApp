package com.example.spring3.mapper;


import com.example.spring3.dto.request.PermissionRequest;
import com.example.spring3.dto.request.RoleRequest;
import com.example.spring3.dto.response.PermissionResponse;
import com.example.spring3.dto.response.RoleResponse;
import com.example.spring3.entity.Permission;
import com.example.spring3.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}

