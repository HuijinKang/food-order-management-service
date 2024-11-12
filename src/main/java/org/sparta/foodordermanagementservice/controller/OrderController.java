package org.sparta.foodordermanagementservice.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.common.PageSizeRule;
import org.sparta.foodordermanagementservice.dto.OrderDTO;
import org.sparta.foodordermanagementservice.dto.ReadOrderListDto;
import org.sparta.foodordermanagementservice.dto.request.OrderListRequestCondition;
import org.sparta.foodordermanagementservice.dto.request.SortedBy;
import org.sparta.foodordermanagementservice.dto.response.OrderListResObj;
import org.sparta.foodordermanagementservice.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor

@RestController
@RequestMapping("api/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ApiResponse<List<OrderListResObj>> readOrderList
            (
                    @RequestParam OrderListRequestCondition condition,
                    @RequestParam String key,
                    @RequestParam int pageSize,
                    @RequestParam int pageNumber,
                    @RequestParam SortedBy sortedBy,
                    @RequestParam boolean isAsc
            ) {

        if (!PageSizeRule.isPageSizeValid(pageSize))
            pageSize = PageSizeRule.DEFAULT_PAGE_SIZE;

        ReadOrderListDto dto
                = ReadOrderListDto.builder()
                .condition(condition)
                .key(key)
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .sortedBy(sortedBy)
                .isAsc(isAsc)
                .build();

        List<OrderDTO> orderList
                = orderService.searchOrderList(dto);

        List<OrderListResObj> responseObjList
                = orderList.stream()
                .map(OrderListResObj::from)
                .collect(Collectors.toList());

        return ApiResponse.ofSuccess(responseObjList);
    }

//    @GetMapping("/{id}")
//    public ApiResponse<SearchOrderDetailRes> searchOrderDetail(@PathVariable long id) {
//
//        OrderDTO searchedOrder
//                = orderService.searchOrder(id);
//
//        SearchOrderDetailRes orderDetailRes
//                = SearchOrderDetailRes.from(searchedOrder);
//
//        return ApiResponse.ofSuccess(orderDetailRes);
//    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteOrder(@PathVariable UUID id) {

        orderService.deleteOrder(id);

        return ApiResponse.ofSuccess(null);
    }
}

