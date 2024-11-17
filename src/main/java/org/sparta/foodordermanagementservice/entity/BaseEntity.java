package org.sparta.foodordermanagementservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(nullable = false, length = 100, updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(length = 100)
    private String updatedBy;

}