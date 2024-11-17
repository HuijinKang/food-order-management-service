package org.sparta.foodordermanagementservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderStatus;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderType;

import java.time.LocalDateTime;
import java.util.UUID;

@SuppressWarnings("unused")



@ToString

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
@Table(name = "p_order")
public class Order extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false),
            @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)})
    private final User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private final Store store;

    @Setter
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(nullable = false)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(nullable = false)
    private final OrderType type;

    @Column(nullable = false)
    private final String address;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private final int totalPrice;

//    @Column(nullable = false)
//    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

//    @Column
//    private LocalDateTime updatedAt;

    @Column
    private String updatedBy;

    @Setter
    @Column
    private LocalDateTime deletedAt;

    @Setter
    @Column
    private String deletedBy;

    @Builder
    public Order(User user,
                 Store store,
                 OrderStatus status,
                 OrderType type,
                 String address,
                 String comment,
                 int totalPrice,
                 String createdBy) {
        this.user = user;
        this.store = store;
        this.status = status;
        this.type = type;
        this.address = address;
        this.comment = comment;
        this.totalPrice = totalPrice;
    }
}