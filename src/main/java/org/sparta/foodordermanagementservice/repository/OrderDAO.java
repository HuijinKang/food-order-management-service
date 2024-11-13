package org.sparta.foodordermanagementservice.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.PaginateOrdersDTO;
import org.sparta.foodordermanagementservice.entity.Order;
import org.sparta.foodordermanagementservice.entity.QOrder;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderSpec;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
@Slf4j

@Repository
@RequiredArgsConstructor
public class OrderDAO {

    private final OrderJpaRepository orderJpaRepo;

    private final QOrder order = QOrder.order;
    private final JPAQueryFactory queryFactory;

    public long countTotalPages(PaginateOrdersDTO dto) {

        return queryFactory
                .selectFrom(order)
                .where(
                        storeIdEq(dto.getStoreId()),
                        userNameEq(dto.getUserName()),
                        order.deletedAt.isNull()
                )
                .orderBy(OrderSpec.of(dto.getSortedBy(), dto.isAsc()))
                .fetch()
                .size();
    }

    public List<Order> readCurrentPage(PaginateOrdersDTO dto) {

        return queryFactory
                .selectFrom(order)
                .where(
                        storeIdEq(dto.getStoreId()),
                        userNameEq(dto.getUserName()),
                        order.deletedAt.isNull()
                )
                .orderBy(OrderSpec.of(dto.getSortedBy(), dto.isAsc()))
                .offset(dto.getPageSize() * dto.getPageNumber())
                .limit(dto.getPageSize())
                .fetch();

    }

    protected BooleanExpression storeIdEq(UUID storeId) {

        if (storeId == null) return null;

        return order.store.id.eq(storeId);
    }


    protected BooleanExpression userNameEq(String userName) {

        if (userName == null) return null;

//      return order.user.username.eq(dto.getUserName()); //todo 임시, user 구현되면 이걸로 쓰기
        return order.userName.eq(userName);
    }


    public Order readOrder(UUID orderId) {
        return orderJpaRepo.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESOURCE));
    }

    public void softDeleteOrder(UUID orderId, String deleterName) {

        Order order = orderJpaRepo.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESOURCE));

        order.setDeletedAt(LocalDateTime.now());
        order.setDeletedBy("system");//todo auth에서 로그인아이디 받아오도록 수정

        orderJpaRepo.save(order);
    }
}
