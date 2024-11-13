package org.sparta.foodordermanagementservice.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.common.PageSizeRule;
import org.sparta.foodordermanagementservice.dto.PaginateOrdersDTO;
import org.sparta.foodordermanagementservice.dto.request.ReqPaginateOrders;
import org.sparta.foodordermanagementservice.dto.response.ResPagedOrderObj;
import org.sparta.foodordermanagementservice.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor

@RestController
@RequestMapping("api/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ApiResponse<Page<ResPagedOrderObj>> paginateOrders(@ModelAttribute ReqPaginateOrders request) {

        if (!PageSizeRule.validate
                (request.getPageSize())) {

            request.setPageSize(PageSizeRule.DEFAULT_PAGE_SIZE);
        }

        PaginateOrdersDTO dto
                = PaginateOrdersDTO.from(request);

        Page<ResPagedOrderObj> pagedResObjs
                = orderService.paginateOrders(dto);

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

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteOrder(@PathVariable UUID id) {

        orderService.deleteOrder(id);

        return ApiResponse.ofSuccess(null);
    }
}
