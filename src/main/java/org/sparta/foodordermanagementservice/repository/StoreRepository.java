package org.sparta.foodordermanagementservice.repository;

import org.sparta.foodordermanagementservice.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID>, StoreCustomRepository {
    // 가게 이름 중복 체크
    boolean existsByName(String name);
}
