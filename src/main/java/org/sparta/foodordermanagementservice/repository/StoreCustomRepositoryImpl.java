package org.sparta.foodordermanagementservice.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.entity.QCategory;
import org.sparta.foodordermanagementservice.entity.QStore;
import org.sparta.foodordermanagementservice.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoreCustomRepositoryImpl implements StoreCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Store> searchStores(String keyword, Pageable pageable) {
        QStore store = QStore.store;
        QCategory category = QCategory.category;

        List<Store> results = queryFactory
                .selectDistinct(store)
                .from(store)
                .leftJoin(store.categories, category)
                .where(
                        store.deletedAt.isNull()    // 삭제되지 않은 가게만 포함
                                .and(category.deletedAt.isNull())   // 삭제되지 않은 카테고리만 포함
                                .and(store.name.containsIgnoreCase(keyword)
                                        .or(category.name.containsIgnoreCase(keyword)))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(toOrderSpecifiers(pageable.getSort(), store))
                .fetch();

        long total = queryFactory
                .select(store.countDistinct())
                .from(store)
                .leftJoin(store.categories, category)
                .where(
                        store.deletedAt.isNull() // 삭제되지 않은 가게만 포함
                                .and(category.deletedAt.isNull())   // 삭제되지 않은 카테고리만 포함
                                .and(store.name.containsIgnoreCase(keyword)
                                        .or(category.name.containsIgnoreCase(keyword)))
                )
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

    // 정렬 기준 생성 로직
    private OrderSpecifier<?>[] toOrderSpecifiers(Sort sort, QStore store) {
        return sort.stream()
                .map(order -> {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    String property = order.getProperty();

                    // 지원하는 정렬 필드 확인
                    if ("createdAt".equals(property)) {
                        return new OrderSpecifier<>(direction, store.createdAt);
                    } else if ("updatedAt".equals(property)) {
                        return new OrderSpecifier<>(direction, store.updatedAt);
                    } else {
                        throw new IllegalArgumentException("Invalid sort field: " + property);
                    }
                })
                .toArray(OrderSpecifier[]::new);
    }
}
