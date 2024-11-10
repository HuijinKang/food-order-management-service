package org.sparta.foodordermanagementservice.entity.enumerate;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import org.sparta.foodordermanagementservice.dto.request.SortedBy;
import org.sparta.foodordermanagementservice.entity.QOrder;

public class OrderSpec {
    public static OrderSpecifier of(SortedBy sortedBy, boolean isAsc) {

        QOrder order = QOrder.order;

        ComparableExpressionBase sortCriteria
                = sortedBy == SortedBy.UPDATED_AT
                ? order.updatedAt
                : order.createdAt;

        OrderSpecifier sortSpec
                = isAsc
                ? sortCriteria.asc()
                : sortCriteria.desc();

        return sortSpec;
    }

}
