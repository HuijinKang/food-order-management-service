package org.sparta.foodordermanagementservice.repository;

import org.sparta.foodordermanagementservice.entity.Menu;
import org.sparta.foodordermanagementservice.entity.MenuStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuRepository extends JpaRepository<Menu, UUID>, MenuCustomRepository {

    List<Menu> findByStoreIdAndStatusNot(UUID storeId, MenuStatus menuStatus);

    Optional<Menu> findByIdAndStatusNot(UUID menuId, MenuStatus status);
}
