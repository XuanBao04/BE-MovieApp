package com.example.spring3.dto.request;

import com.example.spring3.validator.DobConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
     String password;
     String firstName;
     String lastName;
    @DobConstraint(min= 2,message = "INVALID_DOB")
     LocalDate dob;
     List<String> roles;
}
