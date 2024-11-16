package org.sparta.foodordermanagementservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_category")
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JsonIgnore // 순환 참조 방지
    @ManyToMany(mappedBy = "categories")
    private Set<Store> stores;

    @Column(nullable = false)
    private String name;

    @Column
    private LocalDateTime deletedAt;

    @Column(length = 100)
    private String deletedBy;
}
