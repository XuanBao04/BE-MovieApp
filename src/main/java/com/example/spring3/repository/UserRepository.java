package com.example.spring3.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.spring3.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
