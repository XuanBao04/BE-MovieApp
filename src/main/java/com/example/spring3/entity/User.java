package com.example.spring3.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", length = 50)
    String id;

    @Column(name = "full_name")
    String fullName;

    String password;

    String email;
    String phone;

    @Column(name = "avatar_url")
    String avatarUrl;

    String role;
}
