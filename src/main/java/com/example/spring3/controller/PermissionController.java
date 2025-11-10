package com.example.spring3.controller;

import com.example.spring3.dto.request.ApiResponse;
import com.example.spring3.dto.request.PermissionRequest;
import com.example.spring3.dto.response.PermissionResponse;
import com.example.spring3.entity.Permission;
import com.example.spring3.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/permissions")
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request){
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll(){
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<Void> delete(@PathVariable String permission){
        permissionService.delete(permission);
        return ApiResponse.<Void>builder().build();
    }
}

