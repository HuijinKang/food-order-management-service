package org.sparta.foodordermanagementservice.controller;


import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.common.PageSizeRule;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.CreateOrderDto;
import org.sparta.foodordermanagementservice.dto.PaginateOrdersDTO;
import org.sparta.foodordermanagementservice.dto.ReqUpdateOrder;
import org.sparta.foodordermanagementservice.dto.UpdateOrderStatusDto;
import org.sparta.foodordermanagementservice.dto.request.PaginateOrdersReqCondition;
import org.sparta.foodordermanagementservice.dto.request.ReqCreateOrder;
import org.sparta.foodordermanagementservice.dto.request.SortedBy;
import org.sparta.foodordermanagementservice.dto.response.ResPagedOrderObj;
import org.sparta.foodordermanagementservice.dto.response.ResReadOrderDetail;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderStatus;
import org.sparta.foodordermanagementservice.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.sparta.foodordermanagementservice.common.utils.RoleUtils.hasCustomerRole;
import static org.sparta.foodordermanagementservice.common.utils.RoleUtils.hasOwnerRole;

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;


    @GetMapping
    public ApiResponse<Page<ResPagedOrderObj>> paginateOrders
            (
                    @RequestParam PaginateOrdersReqCondition condition,
                    @RequestParam String key,
                    @RequestParam int pageSize,
                    @RequestParam int pageNumber,
                    @RequestParam SortedBy sortedBy,
                    @RequestParam boolean isAsc,
                    @AuthenticationPrincipal UserDetails userDetails
            ) {

        if (!PageSizeRule.validate(pageSize)) {
            pageSize = PageSizeRule.DEFAULT_PAGE_SIZE;
        }

        PaginateOrdersDTO dto
                = PaginateOrdersDTO.builder()
                .storeId(condition == PaginateOrdersReqCondition.STORE_ID
                        ? UUID.fromString(key)
                        : null
                )
                .username(condition == PaginateOrdersReqCondition.USER_NAME
                        ? key
                        : null
                )
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .sortedBy(sortedBy)
                .isAsc(isAsc)
                .build();

        log.info("hihi " + dto.toString());

        Page<ResPagedOrderObj> pagedResObjs
                = orderService.paginateOrders(dto, userDetails);

        return ApiResponse.ofSuccess(pagedResObjs);
    }

    @GetMapping("/{id}")
    public ApiResponse<ResReadOrderDetail> readOrderDetail
            (
                    @PathVariable UUID id,
                    @AuthenticationPrincipal UserDetails userDetails
            ) {

        ResReadOrderDetail response
                = orderService.readOrderDetail(id, userDetails);

        return ApiResponse.ofSuccess(response);
    }

    @PostMapping
    @Secured(UserRole.Authority.CUSTOMER)
    public ApiResponse<UUID> createOrder
            (@RequestBody ReqCreateOrder request,
             @AuthenticationPrincipal UserDetails userDetails) {

        UUID createdOrderId
                = orderService.createOrder(CreateOrderDto.from(request, userDetails));

        return ApiResponse.ofSuccess(createdOrderId);
    }

    @PatchMapping("/{id}")
    public ApiResponse<Void> updateOrder
            (
                    @PathVariable UUID id,
                    @RequestBody ReqUpdateOrder request,
                    @AuthenticationPrincipal UserDetails userDetails
            ) {

        if ((hasCustomerRole(userDetails)
                && request.getOrderStatus() != OrderStatus.CUSTOMER_CANCEL)
                ||
                (hasOwnerRole(userDetails)
                        && !(request.getOrderStatus() == OrderStatus.ACCEPT || request.getOrderStatus() == OrderStatus.REJECT))
        ) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        UpdateOrderStatusDto dto
                = UpdateOrderStatusDto.from(request, userDetails);

        orderService.updateOrder(id, dto);

        return ApiResponse.ofSuccess(null);
    }

    @Secured(UserRole.Authority.MASTER)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteOrder
            (
                    @PathVariable @NotNull UUID id,
                    @AuthenticationPrincipal UserDetails userDetails
            ) {

        orderService.deleteOrder(id, userDetails);

        return ApiResponse.ofSuccess(null);
    }
}
