package com.example.spring3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.spring3.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);
}
