package com.example.spring3.configuration;

import com.example.spring3.entity.User;
import com.example.spring3.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
       return args -> {
           if(userRepository.findByEmail("admin@cinema.com").isEmpty()){
               User user = User.builder()
                       .email("admin@cinema.com")
                       .password(passwordEncoder.encode("admin"))
                       .role("ADMIN")
                       .build();
               userRepository.save(user);
               log.warn("admin user has been created with default password: admin");
           }
       };
   }

}
