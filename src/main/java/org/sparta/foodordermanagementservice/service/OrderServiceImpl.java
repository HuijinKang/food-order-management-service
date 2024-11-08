package org.sparta.foodordermanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.OrderDTO;
import org.sparta.foodordermanagementservice.dto.SearchOrderListDTO;
import org.sparta.foodordermanagementservice.dto.SelectOrderListDTO;
import org.sparta.foodordermanagementservice.repository.OrderDbTransactionalFacade;

import java.util.List;


@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderDbTransactionalFacade orderRepository;

    @Override
    public List<OrderDTO> searchOrderList(SearchOrderListDTO dto) {

        return orderRepository.selectOrderList(SelectOrderListDTO.from(dto));
    }

}
