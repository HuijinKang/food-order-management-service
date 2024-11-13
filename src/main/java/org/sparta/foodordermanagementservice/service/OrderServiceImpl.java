package org.sparta.foodordermanagementservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.dto.PaginateOrdersDTO;
import org.sparta.foodordermanagementservice.dto.response.ResPagedOrderObj;
import org.sparta.foodordermanagementservice.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    //    @Override
    public Page<ResPagedOrderObj> paginateOrders(PaginateOrdersDTO dto) {

        //todo auth 완료시 접근권한 체크할것, 접근 권한 관련 validation 및 dto 필드값 지워주는 클래스 생성해 사용할것
        //todo 컨트롤러 부터 pageable작업해오기
        List<ResPagedOrderObj> pageContent
                = repository.readCurrentPageOrders(dto)
                .stream()
                .map(ResPagedOrderObj::from)
                .toList();

        long totalPages
                = repository.countTotalPages(dto);

        Pageable pageable
                = PageRequest.of(dto.getPageNumber(), dto.getPageSize());

        return new PageImpl<>(pageContent, pageable, totalPages);
    }


    @Override
    public void deleteOrder(UUID orderId) {

        String deleterName
                = "test"; //todo auth 완료시 로그인한 유저의 이름으로 변경할것, auth.getPrincipal().getName() 이용

        repository.deleteOrder(orderId, deleterName);
    }

}
