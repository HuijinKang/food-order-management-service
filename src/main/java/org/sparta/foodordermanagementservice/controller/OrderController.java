package org.sparta.foodordermanagementservice.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.common.PageSizeRule;
import org.sparta.foodordermanagementservice.domain.Order;
import org.sparta.foodordermanagementservice.dto.SearchOrderListDto;
import org.sparta.foodordermanagementservice.dto.request.OrderListRequestCondition;
import org.sparta.foodordermanagementservice.dto.request.SortedBy;
import org.sparta.foodordermanagementservice.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
@SuppressWarnings("unused")

@RestController
@RequestMapping("api/orders")

@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ApiResponse<List<OrderListResponseObj>> searchOrderList
            (
                    @RequestParam OrderListRequestCondition condition,
                    @RequestParam Long key,
                    @RequestParam int pageSize,
                    @RequestParam int pageNumber,
                    @RequestParam SortedBy sortedBy,
                    @RequestParam boolean isAsc
            ) {

        if (!PageSizeRule.isPageSizeValid(pageSize))
            pageSize = PageSizeRule.DEFAULT_PAGE_SIZE;

        SearchOrderListDto dto
                = SearchOrderListDto.builder()
                .condition(condition)
                .key(key)
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .sortedBy(sortedBy)
                .isAsc(isAsc)
                .build();

        List<Order> searchedOrderList
                = orderService.searchOrderList(dto);

        List<OrderListResponseObj> responseObjList
                = searchedOrderList.stream()
                .map(OrderListResponseObj::from)
                .collect(Collectors.toList());

        return ApiResponse.ofSuccess(responseObjList);
    }
}
