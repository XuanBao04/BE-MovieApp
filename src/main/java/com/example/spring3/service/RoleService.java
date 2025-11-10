package com.example.spring3.service;

import com.example.spring3.dto.request.RoleRequest;
import com.example.spring3.dto.response.RoleResponse;
import com.example.spring3.mapper.RoleMapper;
import com.example.spring3.repository.PermissionRepository;
import com.example.spring3.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    public RoleResponse create(RoleRequest request){
       var role = roleMapper.toRole(request);

       var permissions =   permissionRepository.findAllById(request.getPermissions());

       role.setPermissions(new HashSet<>(permissions));

      role = roleRepository.save(role);

      return  roleMapper.toRoleResponse(role);
    }


    public List<RoleResponse> getAll(){
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void delete(String role){
        roleRepository.deleteById(role);
    }
}
