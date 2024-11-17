package org.sparta.foodordermanagementservice.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@TestConfiguration
public class TestConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        // 테스트용으로 고정된 사용자 이름 반환
        return () -> Optional.of("testUser");
    }

}
