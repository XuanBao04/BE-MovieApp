package com.example.spring3.dto.request;

import com.example.spring3.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    @Size(min =3, message = "USERNAME_INVALID")
     String username;
    @Size(min= 8,message = "PASSWORD_INVALID")
     String password;
     String firstName;
     String lastName;
     @DobConstraint(min= 16,message = "INVALID_DOB")
     LocalDate dob;
}
