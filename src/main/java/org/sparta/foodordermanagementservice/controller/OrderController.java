package org.sparta.foodordermanagementservice.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor

@RestController
@RequestMapping("api/orders")
public class OrderController {
//
//    private final OrderService orderService;
//
//    @GetMapping
//    public ApiResponse<List<OrderListResObj>> readOrderList
//            (
//                    @RequestParam OrderListRequestCondition condition,
//                    @RequestParam String key,
//                    @RequestParam int pageSize,
//                    @RequestParam int pageNumber,
//                    @RequestParam SortedBy sortedBy,
//                    @RequestParam boolean isAsc
//            ) {
//
//        if (!PageSizeRule.validate(pageSize))
//            pageSize = PageSizeRule.DEFAULT_PAGE_SIZE;
//
//        ReadOrderListDto dto
//                = ReadOrderListDto.builder()
//                .condition(condition)
//                .key(key)
//                .pageSize(pageSize)
//                .pageNumber(pageNumber)
//                .sortedBy(sortedBy)
//                .isAsc(isAsc)
//                .build();
//
//        List<OrderDTO> orderList
//                = orderService.readOrderList(dto);
//
//        List<OrderListResObj> responseObjList
//                = orderList.stream()
//                .map(OrderListResObj::from)
//                .collect(Collectors.toList());
//
//        return ApiResponse.ofSuccess(responseObjList);
//    }
//
////    @GetMapping("/{id}")
////    public ApiResponse<SearchOrderDetailRes> searchOrderDetail(@PathVariable long id) {
////
////        OrderDTO searchedOrder
////                = orderService.searchOrder(id);
////
////        SearchOrderDetailRes orderDetailRes
////                = SearchOrderDetailRes.from(searchedOrder);
////
////        return ApiResponse.ofSuccess(orderDetailRes);
////    }
//
//    @DeleteMapping("/{id}")
//    public ApiResponse<Void> deleteOrder(@PathVariable UUID id) {
//
//        orderService.deleteOrder(id);
//
//        return ApiResponse.ofSuccess(
//                null
//        );
//    }
}
