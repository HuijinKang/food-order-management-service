package org.sparta.foodordermanagementservice.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.service.OrderService;
import org.sparta.foodordermanagementservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/orders")

@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ApiResponse<List<GetOrderListResponse>> getOrderList
            (
                    @RequestParam GetOrderListReqCondition condition,
                    @RequestParam Long key,
                    @RequestParam int pageSize,
                    @RequestParam int pageNubmer,
                    @RequestParam SortedBy sortedBy,
                    @RequestParam boolean isAsc
            )
    {
        if (pageSize != 10 && pageSize != 30 &&pageSize != 50 ) //todo 페이지 사이즈 매직넘버 객체로 묶기
            pageSize = 10;
        return ApiResponse.ofSuccess(null);
    }


}
