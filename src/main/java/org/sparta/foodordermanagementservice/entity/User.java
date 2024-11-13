package org.sparta.foodordermanagementservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "p_user", indexes = {
        @Index(name = "users_idx_username", columnList = "username")
})
public class User extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Column(nullable = false, unique = true, length = 100)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @Column(nullable = false)
    private Boolean isPublic;

//    @Column(nullable = false, updatable = false)
//    @CreatedDate
//    private LocalDateTime createdAt;

    @Column(nullable = false, length = 100, updatable = false)
    @CreatedBy
    private String createdBy;

//    @Column(nullable = false)
//    @LastModifiedDate
//    private LocalDateTime updatedAt;

    @Column(nullable = false, length = 100)
    @LastModifiedBy
    private String updatedBy;

    @Column
    private LocalDateTime deletedAt;

    @Column(length = 100)
    private String deletedBy;
}
