package com.example.spring3.configuration;

public class PublicEndpoints {
        public static final String[] POST = {
                        "/users",
                        "/auth/login",
                        "/auth/introspect",
                        "/auth/logout",
                        "/auth/refresh"
        };

        public static final String[] GET = {
                        "/movies/**",
                        "/showtimes/**",
                        "/payment/**",
                        "/seat/**"
        };
}
