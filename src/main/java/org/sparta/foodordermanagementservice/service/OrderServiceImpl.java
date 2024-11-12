package org.sparta.foodordermanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.OrderDTO;
import org.sparta.foodordermanagementservice.dto.ReadOrderListDto;
import org.sparta.foodordermanagementservice.dto.SelectOrderListDTO;
import org.sparta.foodordermanagementservice.repository.OrderPersistenceFacade;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderPersistenceFacade persistence;

    @Override
    public List<OrderDTO> readOrderList(ReadOrderListDto dto) {

        //todo auth 완료시 접근권한 체크할것, 접근 권한 관련 validation클래스 생성해 사용할것

        return persistence.readOrderList(SelectOrderListDTO.from(dto));
    }

    @Override
    public void deleteOrder(UUID orderId) {

        String deleterName
                = "test"; //todo auth 완료시 로그인한 유저의 이름으로 변경할것, auth.getPrincipal().getName() 이용

        persistence.deleteOrder(orderId, deleterName);
    }

}
