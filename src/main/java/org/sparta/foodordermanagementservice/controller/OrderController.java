package org.sparta.foodordermanagementservice.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.common.PageSizeRule;
import org.sparta.foodordermanagementservice.dto.ReadOrderListDto;
import org.sparta.foodordermanagementservice.dto.request.ReqReadOrderList;
import org.sparta.foodordermanagementservice.dto.response.ResReadOrderListObj;
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
    public ApiResponse<List<ResReadOrderListObj>> readOrderList(@ModelAttribute ReqReadOrderList request) {

        ReadOrderListDto dto = ReadOrderListDto.from(request);

        if (!PageSizeRule.validate(dto.getPageSize()))
            dto.setPageSize(PageSizeRule.DEFAULT_PAGE_SIZE);

        List<ResReadOrderListObj> responseObjList
                = orderService.readOrderList(dto)
                .stream()
                .map(ResReadOrderListObj::from)
                .collect(Collectors.toList());

        return ApiResponse.ofSuccess(responseObjList);
    }

//    @GetMapping("/{id}")
//    public ApiResponse<ResReadOrder> searchOrderDetail(@PathVariable long id) {
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
