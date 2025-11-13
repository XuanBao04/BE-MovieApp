package com.example.spring3.mapper;




import com.example.spring3.dto.request.UserCreateRequest;
import com.example.spring3.dto.request.UserUpdateRequest;
import com.example.spring3.dto.response.UserResponse;
import com.example.spring3.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequest request);
    UserResponse toUserResponse(User user);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}

