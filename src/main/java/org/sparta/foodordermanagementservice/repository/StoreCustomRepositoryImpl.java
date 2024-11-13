package org.sparta.foodordermanagementservice.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.entity.QCategory;
import org.sparta.foodordermanagementservice.entity.QStore;
import org.sparta.foodordermanagementservice.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import java.util.List;

@RequiredArgsConstructor
public class StoreCustomRepositoryImpl implements StoreCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Store> searchStores(String categoryKeyword, String nameKeyword, Pageable pageable) {
        QStore store = QStore.store;
        QCategory category = QCategory.category;

        List<Store> results = queryFactory
                .selectDistinct(store)
                .from(store)
                .leftJoin(store.categories, category)
                .where(
                        categoryContains(categoryKeyword),
                        nameContains(nameKeyword)
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
                        categoryContains(categoryKeyword),
                        nameContains(nameKeyword)
                )
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression categoryContains(String categoryKeyword) {
        return categoryKeyword != null ? QCategory.category.name.containsIgnoreCase(categoryKeyword) : null;
    }

    private BooleanExpression nameContains(String nameKeyword) {
        return nameKeyword != null ? QStore.store.name.containsIgnoreCase(nameKeyword) : null;
    }

    private OrderSpecifier<?>[] toOrderSpecifiers(Sort sort, QStore store) {
        return sort.stream()
                .map(order -> {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    switch (order.getProperty()) {
                        case "createdAt":
                            return new OrderSpecifier<>(direction, store.createdAt);
                        case "updatedAt":
                            return new OrderSpecifier<>(direction, store.updatedAt);
                        default:
                            throw new IllegalArgumentException("Invalid sort field: " + order.getProperty());
                    }
                })
                .toArray(OrderSpecifier[]::new);
    }
}
