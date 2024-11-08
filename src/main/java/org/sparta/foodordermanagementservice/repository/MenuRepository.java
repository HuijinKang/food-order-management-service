package org.sparta.foodordermanagementservice.repository;

import org.sparta.foodordermanagementservice.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MenuRepository extends JpaRepository<Menu, UUID> {
}
