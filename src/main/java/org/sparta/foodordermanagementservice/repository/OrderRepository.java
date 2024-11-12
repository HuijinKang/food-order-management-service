package org.sparta.foodordermanagementservice.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.SelectOrderListDTO;
import org.sparta.foodordermanagementservice.entity.Order;
import org.sparta.foodordermanagementservice.entity.QOrder;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderSpec;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")


@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final OrderJpaRepository orderJpaRepo;

    private final JPAQueryFactory queryFactory;
    private final QOrder qOrder = QOrder.order;

    public List<Order> readOrderList(SelectOrderListDTO dto) {

        BooleanExpression storeIdEq
                = dto.getStoreId() == null
                ? null
                : qOrder.store.id.eq(dto.getStoreId());

        BooleanExpression userNameEq
                = dto.getUserName() == null
                ? null
//                : order.user.username.eq(dto.getUserName()); //todo 테스트용, user 구현되면 이걸로 쓰기
                : qOrder.userName.eq(dto.getUserName());


        List<Order> readOrderList
                = queryFactory
                .selectFrom(qOrder)
                .where(
                        storeIdEq,
                        userNameEq,
                        qOrder.deletedAt.isNull()
                )
                .orderBy(OrderSpec.of(dto.getSortedBy(), dto.isAsc()))
                .offset(dto.getPageSize() * (dto.getPageNumber() - 1))
                .limit(dto.getPageSize())
                .fetch();

        return readOrderList;
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
