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

        // 조건을 조합하여 검색 수행
        List<Store> results = queryFactory
                .selectDistinct(store)
                .from(store)
                .leftJoin(store.categories, category)
                .where(categoryContains(categoryKeyword).or(nameContains(nameKeyword)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(toOrderSpecifiers(pageable.getSort(), store))
                .fetch();

        Long total = queryFactory
                .select(store.countDistinct())
                .from(store)
                .leftJoin(store.categories, category)
                .where(categoryContains(categoryKeyword).or(nameContains(nameKeyword)))
                .fetchOne();

        return new PageImpl<>(results, pageable, total != null ? total : 0);
    }

    // 카테고리 이름이 키워드와 일치하는지 확인하는 조건
    private BooleanExpression categoryContains(String categoryKeyword) {
        return (categoryKeyword != null && !categoryKeyword.isEmpty()) ? QCategory.category.name.containsIgnoreCase(categoryKeyword) : null;
    }

    // 가게 이름이 키워드와 일치하는지 확인하는 조건
    private BooleanExpression nameContains(String nameKeyword) {
        return (nameKeyword != null && !nameKeyword.isEmpty()) ? QStore.store.name.containsIgnoreCase(nameKeyword) : null;
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
