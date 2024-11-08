package org.sparta.foodordermanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.domain.Order;
import org.sparta.foodordermanagementservice.dto.SearchOrderListDto;
import org.sparta.foodordermanagementservice.dto.SelectOrderListDto;
import org.sparta.foodordermanagementservice.repository.OrderDbTransactionalFacade;

import java.util.List;


@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderDbTransactionalFacade orderRepository;
    @Override
    public List<Order> searchOrderList(SearchOrderListDto dto) {

        return orderRepository.selectOrderList(SelectOrderListDto.from(dto));
    }

}
