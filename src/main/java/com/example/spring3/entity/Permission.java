package com.example.spring3.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission {
    @Id
    String name;
    String description;

}
