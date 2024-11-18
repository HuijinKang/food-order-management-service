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
@Table(name = "p_payment")
public class Payment extends BaseEntity{

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

    @Column(nullable = false, length = 255)
    private String paymentType;

    @Column(nullable = false, length = 1000)
    private String receipt;

//    @Column(nullable = false)
//    private LocalDateTime createdAt;
//
//    @Column(nullable = false)
//    private String createdBy;
//
//    @Column
//    private LocalDateTime updatedAt;
//
//    @Column
//    private String updatedBy;

    @Column(length = 255)
    private String updatedBy;

    @Column
    private LocalDateTime deletedAt;

    @Column(length = 255)
    private String deletedBy;

    @Setter
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(nullable = false)
    private PaymentStatus status;

    @Setter
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
