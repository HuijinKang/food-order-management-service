package org.sparta.foodordermanagementservice.repository;

import org.sparta.foodordermanagementservice.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MenuCustomRepository {
    Page<Menu> searchMenus(String condition, String keyword, Pageable pageable);
}
