package com.example.spring3.service;

import com.example.spring3.dto.request.UserCreateRequest;
import com.example.spring3.dto.request.UserUpdateRequest;
import com.example.spring3.dto.response.UserResponse;
import com.example.spring3.entity.User;
import com.example.spring3.enums.Role;
import com.example.spring3.exception.AppException;
import com.example.spring3.exception.ErrorCode;
import com.example.spring3.mapper.UserMapper;
import com.example.spring3.repository.RoleRepository;
import com.example.spring3.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class UserService {
     RoleRepository roleRepository;
    UserRepository userRepository;
      UserMapper userMapper;
      PasswordEncoder passwordEncoder;
// tạo user
    public User createRequest(UserCreateRequest request){
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXIST);
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.user.name());
       // user.setRoles(roles);
        return userRepository.save(user);
    }
    // lấy all danh sách
//    @PreAuthorize("hasRole('admin')")
    @PreAuthorize("hasAuthority('CREATE_DATA')")
    public List<UserResponse> getUsers() {
        var users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserResponse)
                .toList();
    }
    // lấy 1 user theo id
    @PostAuthorize("returnObject.result.username == authentication.name ")
    public UserResponse getUser(String id){
        log.info("In method get user by id");
        return  userMapper.toUserResponse(userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST)));
    }
    // lấy thông tin cá nhân
    public  UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
      User user = userRepository.findByUsername(name).orElseThrow(
              ()->new AppException(ErrorCode.USER_NOT_EXIST));
      return userMapper.toUserResponse(user);
    }
    // update thông tin user
    public UserResponse updateUser(String userId, UserUpdateRequest request){
       User user = userRepository.findById(userId)
               .orElseThrow(()->new RuntimeException("user not found"));
       userMapper.updateUser(user,request);
       user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (request.getRoles() != null) {
            var roles = roleRepository.findAllById(request.getRoles());
            user.setRoles(new HashSet<>(roles));
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }
    // xóa user
    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }


}
