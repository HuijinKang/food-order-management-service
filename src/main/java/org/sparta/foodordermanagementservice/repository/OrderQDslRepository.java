package org.sparta.foodordermanagementservice.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.SelectOrderListDTO;
import org.sparta.foodordermanagementservice.dto.request.SortedBy;
import org.sparta.foodordermanagementservice.entity.Order;
import org.sparta.foodordermanagementservice.entity.QOrder;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")

@Repository
@RequiredArgsConstructor
public class OrderQDslRepository {

    private final JPAQueryFactory queryFactory;

    public List<Order> selectOrderList(SelectOrderListDTO dto) {

        QOrder order = QOrder.order;

        ComparableExpressionBase sortCriteria
                = dto.getSortedBy() == SortedBy.UPDATED_AT
                ? order.updatedAt
                : order.createdAt;

        OrderSpecifier sortSpec
                = dto.isAsc()
                ? sortCriteria.asc()
                : sortCriteria.desc();

        return queryFactory
                .selectFrom(order)
                .where(order.store.id.eq(dto.getStoreId())
                        .and(order.user.username.eq(dto.getUserName())))
                .orderBy(sortSpec)
                .offset(dto.getPageSize() * dto.getPageNumber())
                .limit(dto.getPageSize())
                .fetch();
    }
}