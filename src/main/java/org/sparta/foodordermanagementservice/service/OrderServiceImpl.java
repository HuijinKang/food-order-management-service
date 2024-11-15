package org.sparta.foodordermanagementservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.dto.OrderDTO;
import org.sparta.foodordermanagementservice.dto.PaginateOrdersDTO;
import org.sparta.foodordermanagementservice.dto.response.ResPagedOrderObj;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;


    @Override
    public Page<ResPagedOrderObj> paginateOrders(PaginateOrdersDTO dto,
                                                 UserDetails userDetails) {

        //todo 컨트롤러 부터 pageable작업해오기
        List<ResPagedOrderObj> pageContent
                = repository.readCurrentPageOrders(dto)
                .stream()
                .map(orderDTO -> {

                    eraseNotAllowedInfo(orderDTO, userDetails.getAuthorities());

                    return ResPagedOrderObj.from(orderDTO);
                })
                .toList();

        long totalPages
                = repository.countTotalPages(dto);

        Pageable pageable
                = PageRequest.of(dto.getPageNumber(), dto.getPageSize());

        return new PageImpl<>(pageContent, pageable, totalPages);
    }

    @Override
    public void deleteOrder(UUID orderId, String userName) {

        String deleterName
                = "test";

        repository.deleteOrder(orderId, deleterName);
    }


    protected void eraseNotAllowedInfo(OrderDTO target,
                                       Collection<? extends GrantedAuthority> authorities) {

        if (authorities.contains(UserRole.Authority.OWNER)
                || authorities.contains(UserRole.Authority.CUSTOMER)) {

            target.setDeletedAt(null);
            target.setDeletedBy(null);
        }

    }
}
