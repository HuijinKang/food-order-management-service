package org.sparta.foodordermanagementservice.repository;

import org.junit.jupiter.api.Test;
import org.sparta.foodordermanagementservice.config.TestConfig;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.entity.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsernameSuccess() {
        User testUser = User.builder()
                .username("testUser")
                .password("testPassword")
                .email("test@email.com")
                .nickname("testNickname")
                .isPublic(true)
                .userRole(UserRole.CUSTOMER)
//                .createdBy("testUser")
//                .updatedBy("testUser")
                .build();

        userRepository.save(testUser);

        Optional<User> savedUser = userRepository.findByUsername(testUser.getUsername());

        assertThat(savedUser.isPresent()).isTrue();
        assertThat(savedUser.get().getId()).isNotNull();
        assertThat(savedUser.get().getUsername()).isEqualTo("testUser");
        assertThat(savedUser.get().getPassword()).isEqualTo("testPassword");
        assertThat(savedUser.get().getEmail()).isEqualTo("test@email.com");
        assertThat(savedUser.get().getNickname()).isEqualTo("testNickname");
        assertThat(savedUser.get().getIsPublic()).isEqualTo(true);
        assertThat(savedUser.get().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(savedUser.get().getUserRole()).isEqualTo(UserRole.CUSTOMER);

    }

    @Test
    void findByUsernameAndStatusSuccess() {
        User testUser = User.builder()
                .username("testUser")
                .password("testPassword")
                .email("test@email.com")
                .nickname("testNickname")
                .isPublic(true)
                .userRole(UserRole.CUSTOMER)
//                .createdBy("testUser")
//                .updatedBy("testUser")
                .build();

        userRepository.save(testUser);

        Optional<User> savedUser = userRepository.findByUsernameAndStatus(testUser.getUsername(), testUser.getStatus());

        assertThat(savedUser.isPresent()).isTrue();
        assertThat(savedUser.get().getId()).isNotNull();
        assertThat(savedUser.get().getUsername()).isEqualTo("testUser");
        assertThat(savedUser.get().getPassword()).isEqualTo("testPassword");
        assertThat(savedUser.get().getEmail()).isEqualTo("test@email.com");
        assertThat(savedUser.get().getNickname()).isEqualTo("testNickname");
        assertThat(savedUser.get().getIsPublic()).isEqualTo(true);
        assertThat(savedUser.get().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(savedUser.get().getUserRole()).isEqualTo(UserRole.CUSTOMER);
    }

    @Test
    void findByUsernameAndStatusFailWhenStatusIsLeave() {
        User testUser = User.builder()
                .username("testUser")
                .password("testPassword")
                .email("test@email.com")
                .nickname("testNickname")
                .isPublic(true)
                .status(UserStatus.LEAVE)
                .userRole(UserRole.CUSTOMER)
//                .createdBy("testUser")
//                .updatedBy("testUser")
                .build();

        userRepository.save(testUser);

        Optional<User> savedUser = userRepository.findByUsernameAndStatus(testUser.getUsername(), UserStatus.ACTIVE);

        assertThat(savedUser.isPresent()).isFalse();
    }

    @Test
    void existsByUsername() {
        User testUser = User.builder()
                .username("testUser")
                .password("testPassword")
                .email("test@email.com")
                .nickname("testNickname")
                .isPublic(true)
                .userRole(UserRole.CUSTOMER)
//                .createdBy("testUser")
//                .updatedBy("testUser")
                .build();

        userRepository.save(testUser);

        assertThat(userRepository.existsByUsername("testUser")).isTrue();
        assertThat(userRepository.existsByUsername("notTestUser")).isFalse();

    }

    @Test
    void existsByNickname() {
        User testUser = User.builder()
                .username("testUser")
                .password("testPassword")
                .email("test@email.com")
                .nickname("testNickname")
                .isPublic(true)
                .userRole(UserRole.CUSTOMER)
//                .createdBy("testUser")
//                .updatedBy("testUser")
                .build();

        userRepository.save(testUser);

        assertThat(userRepository.existsByNickname("testNickname")).isTrue();
        assertThat(userRepository.existsByNickname("notTestNickname")).isFalse();
    }

    @Test
    void existsByEmail() {
        User testUser = User.builder()
                .username("testUser")
                .password("testPassword")
                .email("test@email.com")
                .nickname("testNickname")
                .isPublic(true)
                .userRole(UserRole.CUSTOMER)
//                .createdBy("testUser")
//                .updatedBy("testUser")
                .build();

        userRepository.save(testUser);

        assertThat(userRepository.existsByEmail("test@email.com")).isTrue();
        assertThat(userRepository.existsByEmail("notTest@email.com")).isFalse();
    }
}