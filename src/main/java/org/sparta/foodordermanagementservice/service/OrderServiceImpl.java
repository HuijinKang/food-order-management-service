package org.sparta.foodordermanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.OrderDTO;
import org.sparta.foodordermanagementservice.dto.SearchOrderListDTO;
import org.sparta.foodordermanagementservice.dto.SelectOrderListDTO;
import org.sparta.foodordermanagementservice.repository.OrderDbTransactionalFacade;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderDbTransactionalFacade orderRepository;

    @Override
    public List<OrderDTO> searchOrderList(SearchOrderListDTO dto) {

        //todo auth 완료시 접근권한 체크할것, 접근 권한 관련 validation클래스 생성해 사용할것

        return orderRepository.selectOrderList(SelectOrderListDTO.from(dto));
    }

    @Override
    public void deleteOrder(UUID id) {
        orderRepository.deleteOrder(id);
    }

}
