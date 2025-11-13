package com.example.spring3.controller;

import com.example.spring3.dto.request.ApiResponse;
import com.example.spring3.dto.request.UserCreateRequest;
import com.example.spring3.dto.request.UserUpdateRequest;
import com.example.spring3.dto.response.UserResponse;
import com.example.spring3.entity.User;
import com.example.spring3.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;

    // tạo (đăng ký) người dùng
    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.createRequest(request))
                .build();
    }
    // lấy danh sách tất cả người dùng user
    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Email: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }
    // lấy thông tin user theo id
    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable String userId){

        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }
    // lấy thông tin người dùng đang đăng nhập
    @GetMapping("/me")
    ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }
    // update thông tin user
    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable ("userId") String userId, @RequestBody @Valid UserUpdateRequest user){
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId,user))
                .build();
    }
    // xóa user
    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable("userId") String userId){
        userService.deleteUser(userId);
        return "User had been deleted";
    }


}
