package org.sparta.foodordermanagementservice.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderStatus;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
@Getter
public class UpdateOrderStatusDto {

    private final OrderStatus orderStatus;
    private final UserDetails userDetails;

    @Builder
    public UpdateOrderStatusDto(OrderStatus orderStatus, UserDetails userDetails) {

        this.orderStatus = orderStatus;
        this.userDetails = userDetails;
    }

    public static UpdateOrderStatusDto from(ReqUpdateOrder request, UserDetails userDetails) {
        return new UpdateOrderStatusDto(request.getOrderStatus(), userDetails);
    }
}
