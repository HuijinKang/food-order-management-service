package org.sparta.foodordermanagementservice.repository;

import org.sparta.foodordermanagementservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
}
