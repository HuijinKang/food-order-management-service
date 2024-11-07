package org.sparta.foodordermanagementservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private MenuStatus status = MenuStatus.ACTIVE;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String menuImageUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 100)
    private String createdBy;

    @Column
    private LocalDateTime updatedAt;

    @Column(length = 100)
    private String updatedBy;

    @Column
    private LocalDateTime deletedAt;

    @Column(length = 100)
    private String deletedBy;
}
