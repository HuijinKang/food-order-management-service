package org.sparta.foodordermanagementservice.repository;

import org.sparta.foodordermanagementservice.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
}
