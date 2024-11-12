package org.sparta.foodordermanagementservice.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.SelectOrderListDTO;
import org.sparta.foodordermanagementservice.entity.Order;
import org.sparta.foodordermanagementservice.entity.QOrder;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderSpec;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")

@Repository
@RequiredArgsConstructor
public class OrderQDslRepository {

    private final JPAQueryFactory queryFactory;

    public List<Order> readOrderList(SelectOrderListDTO dto) {

        QOrder order = QOrder.order;

        BooleanExpression storeIdEq
                = dto.getStoreId() == null
                ? null
                : order.store.id.eq(dto.getStoreId());

        BooleanExpression userNameEq
                = dto.getUserName() == null
                ? null
//                : order.user.username.eq(dto.getUserName()); //todo 테스트용, user 구현되면 이걸로 쓰기
                : order.userName.eq(dto.getUserName());


        List<Order> readOrderList
                = queryFactory
                .selectFrom(order)
                .where(
                        storeIdEq,
                        userNameEq,
                        order.deletedAt.isNull()
                )
                .orderBy(OrderSpec.of(dto.getSortedBy(), dto.isAsc()))
                .offset(dto.getPageSize() * (dto.getPageNumber() - 1))
                .limit(dto.getPageSize())
                .fetch();

        return readOrderList;
    }

}