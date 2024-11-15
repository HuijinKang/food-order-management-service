package org.sparta.foodordermanagementservice.controller;


import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.common.PageSizeRule;
import org.sparta.foodordermanagementservice.dto.PaginateOrdersDTO;
import org.sparta.foodordermanagementservice.dto.request.ReqPaginateOrders;
import org.sparta.foodordermanagementservice.dto.response.ResPagedOrderObj;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor

@RestController
@RequestMapping("api/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping                                 //todo JsonCreator로 유효성 검사 넣기, pageSizeRule도 저안에 넣기
    public ApiResponse<Page<ResPagedOrderObj>> paginateOrders
            (
                    @ModelAttribute ReqPaginateOrders request,
                    @AuthenticationPrincipal UserDetails userDetails
            ) {

        if (!PageSizeRule.validate(
                request.getPageSize())) {

            request.setPageSize(PageSizeRule.DEFAULT_PAGE_SIZE);
        }

        PaginateOrdersDTO dto
                = PaginateOrdersDTO.from(request);

        Page<ResPagedOrderObj> pagedResObjs
                = orderService.paginateOrders(dto, userDetails);

        return ApiResponse.ofSuccess(pagedResObjs);
    }

//    @GetMapping("/{id}")
//    public ApiResponse<ResReadOrder> readOrderDetail(@PathVariable long id) {
//
//        OrderDTO searchedOrder
//                = orderService.searchOrder(id);
//
//        ResReadOrder orderDetailRes
//                = ResReadOrder.from(searchedOrder);
//
//        return ApiResponse.ofSuccess(orderDetailRes);
//    }

    @Secured(UserRole.Authority.MASTER)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteOrder
            (
                    @PathVariable @NotBlank UUID id,
                    @AuthenticationPrincipal UserDetails userDetails
            ) {

        orderService.deleteOrder(id, userDetails.getUsername());

        return ApiResponse.ofSuccess(null);
    }
}
