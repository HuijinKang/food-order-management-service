package org.sparta.foodordermanagementservice.repository;

import org.sparta.foodordermanagementservice.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

    // 카테고리 이름으로 검색
    Page<Store> findByCategoriesNameContaining(String keyword, Pageable pageable);

    // 가게 이름으로 검색
    Page<Store> findByNameContaining(String keyword, Pageable pageable);
}
