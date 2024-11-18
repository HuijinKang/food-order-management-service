package org.sparta.foodordermanagementservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_menu")
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private MenuStatus status = MenuStatus.ACTIVE;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String menuImageUrl;

    @Column
    private LocalDateTime deletedAt;

    @Column(length = 255)
    private String deletedBy;

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePrice(int price) {
        this.price = price;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateStatus(MenuStatus status) {
        this.status = status;
    }

    public void updateDeletedAt() {
        this.deletedAt = LocalDateTime.now();
    }

    public void updateDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public void updateMenuImageUrl(String menuImageUrl) {
        this.menuImageUrl = menuImageUrl;
    }

}
