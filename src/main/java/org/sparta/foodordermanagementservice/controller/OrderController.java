package org.sparta.foodordermanagementservice.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/orders")

@RequiredArgsConstructor
@Slf4j
public class OrderController {

//    private final OrderService orderService;

//    @GetMapping
//    public ApiResponse<List<OrderListResponse>> getOrderList
//            (
//                    @RequestParam OrderListRequestCondition condition,
//                    @RequestParam Long key,
//                    @RequestParam int pageSize,
//                    @RequestParam int pageNumber,
//                    @RequestParam SortedBy sortedBy,
//                    @RequestParam boolean isAsc
//            ) {
//
//        if (!PageSizeRule.isPageSizeValid(pageSize))
//            pageSize = PageSizeRule.DEFAULT_PAGE_SIZE;
//
//
//
//        return ApiResponse.ofSuccess(null);
//    }
}
