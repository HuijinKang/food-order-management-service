package org.sparta.foodordermanagementservice.repository;

import org.sparta.foodordermanagementservice.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreCustomRepository {
    Page<Store> searchStores(String keyword, Pageable pageable);
}
