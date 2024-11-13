package org.sparta.foodordermanagementservice.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.sparta.foodordermanagementservice.config.TestConfig;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void findByUsername() {
        User testUser = User.builder()
                .username("testuser")
                .password("testpassword")
                .email("test@email.com")
                .nickname("testnickname")
                .isPublic(true)
                .userRole(UserRole.CUSTOMER)
                .createdBy("testuser")
                .updatedBy("testuser")
                .build();

        userRepository.save(testUser);

        Optional<User> savedUser = userRepository.findByUsername(testUser.getUsername());

        assertThat(savedUser.isPresent()).isTrue();
        assertThat(savedUser.get().getId()).isNotNull();
        assertThat(savedUser.get().getUsername()).isEqualTo("testuser");
        assertThat(savedUser.get().getPassword()).isEqualTo("testpassword");
        assertThat(savedUser.get().getEmail()).isEqualTo("test@email.com");
        assertThat(savedUser.get().getNickname()).isEqualTo("testnickname");
        assertThat(savedUser.get().getIsPublic()).isEqualTo(true);
        assertThat(savedUser.get().getUserRole()).isEqualTo(UserRole.CUSTOMER);

    }

    @Test
    void existsByUsername() {
        User testUser = User.builder()
                .username("testuser")
                .password("testpassword")
                .email("test@email.com")
                .nickname("testnickname")
                .isPublic(true)
                .userRole(UserRole.CUSTOMER)
                .createdBy("testuser")
                .updatedBy("testuser")
                .build();

        userRepository.save(testUser);

        assertThat(userRepository.existsByUsername("testuser")).isTrue();
        assertThat(userRepository.existsByUsername("nottestuser")).isFalse();

    }

    @Test
    void existsByNickname() {
        User testUser = User.builder()
                .username("testuser")
                .password("testpassword")
                .email("test@email.com")
                .nickname("testnickname")
                .isPublic(true)
                .userRole(UserRole.CUSTOMER)
                .createdBy("testuser")
                .updatedBy("testuser")
                .build();

        userRepository.save(testUser);

        assertThat(userRepository.existsByNickname("testnickname")).isTrue();
        assertThat(userRepository.existsByNickname("nottestnickname")).isFalse();
    }

    @Test
    void existsByEmail() {
        User testUser = User.builder()
                .username("testuser")
                .password("testpassword")
                .email("test@email.com")
                .nickname("testnickname")
                .isPublic(true)
                .userRole(UserRole.CUSTOMER)
                .createdBy("testuser")
                .updatedBy("testuser")
                .build();

        userRepository.save(testUser);

        assertThat(userRepository.existsByEmail("test@email.com")).isTrue();
        assertThat(userRepository.existsByEmail("nottest@email.com")).isFalse();
    }
}