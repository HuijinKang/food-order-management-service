package org.sparta.foodordermanagementservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.sparta.foodordermanagementservice.entity.enumerate.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Builder
@AllArgsConstructor
@Table(name = "p_payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false),
            @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)})
    private User user;

    @Column(nullable = false, length = 1000)
    private String receipt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private String updatedBy;

    @Column
    private LocalDateTime deletedAt;

    @Column
    private String deletedBy;

    @Setter
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false)
    private int payedPrice;


    @Builder
    public Payment(Order order, User user, PaymentStatus status, int payedPrice, String receipt) {
        this.order = order;
        this.user = user;
        this.receipt = receipt;
        this.status = status;
        this.payedPrice = payedPrice;
    }
}
