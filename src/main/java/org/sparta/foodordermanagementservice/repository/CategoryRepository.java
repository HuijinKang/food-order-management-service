package org.sparta.foodordermanagementservice.repository;

import org.sparta.foodordermanagementservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
