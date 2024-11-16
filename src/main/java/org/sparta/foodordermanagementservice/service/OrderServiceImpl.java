package org.sparta.foodordermanagementservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.dto.OrderDTO;
import org.sparta.foodordermanagementservice.dto.PaginateOrdersDTO;
import org.sparta.foodordermanagementservice.dto.PaymentDTO;
import org.sparta.foodordermanagementservice.dto.response.ResOrderedMenu;
import org.sparta.foodordermanagementservice.dto.response.ResPagedOrderObj;
import org.sparta.foodordermanagementservice.dto.response.ResReadOrderDetail;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.repository.OrderRepository;
import org.sparta.foodordermanagementservice.repository.OrderedMenuRepository;
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

    private final OrderRepository orderRepo;
    private final OrderedMenuRepository orderedMenuRepo;
    private final PaymentRepository paymentRepo;


    @Override
    public Page<ResPagedOrderObj> paginateOrders(PaginateOrdersDTO dto,
                                                 UserDetails userDetails) {

        //todo 컨트롤러 부터 pageable작업해오기
        List<ResPagedOrderObj> pageContent
                = orderRepo.readCurrentPageOrders(dto)
                .stream()
                .map(orderDTO -> {
                    log.info("service " + orderDTO.toString());
                    eraseNotAllowedInfo(orderDTO, userDetails.getAuthorities());
                    ResPagedOrderObj res = ResPagedOrderObj.from(orderDTO);
                    log.info("res herer" + res.toString());
                    return res;
                })
                .toList();

        long totalOrders
                = orderRepo.countTotalOrders(dto.getStoreId(), dto.getUsername());

        Pageable pageable
                = PageRequest.of(dto.getPageNumber(), dto.getPageSize());

        return new PageImpl<>(pageContent, pageable, totalOrders);
    }

    @Override
    public void deleteOrder(UUID orderId, UserDetails userDetails) {

        String deleterName
                = userDetails.getUsername();

        orderRepo.deleteOrder(orderId, deleterName);
    }


//    @Override
//    public UUID createOrder(CreateOrderDto dto) {
//
//        //total price calculation
//        double totalPrice = dto.getOrderedMenuList()
//                .stream()
//                .mapToDouble(orderedMenu -> orderedMenu.getMenu().getPrice() * orderedMenu.getQuantity())
//                .sum();
//
//        //payment
//        PaymentDTO payment = PaymentDTO.builder()
//                .totalPrice(totalPrice)
//                .build();
//
//        //create Order
//        UUID createdId = orderRepo.createOrder(dto);
//
//
//        //create orderedMenu
//
//        return createdId;
//    }

    @Override
    public ResReadOrderDetail readOrderDetail(UUID orderId, UserDetails userDetails) {

        OrderDTO order = orderRepo.readOrder(orderId);

        List<ResOrderedMenu> menuList
                = orderedMenuRepo.readOrderedMenuList(orderId)
                .stream()
                .map(ResOrderedMenu::from)
                .toList();

        boolean paymentInfoAccessible
                = userDetails.getAuthorities().contains(UserRole.Authority.MANAGER)
                || userDetails.getAuthorities().contains(UserRole.Authority.CUSTOMER);

        PaymentDTO payment
                = paymentInfoAccessible
                ? PaymentDTO.from(paymentRepo.readPayment())
                : null;

        return ResReadOrderDetail.from(order, menuList, payment);
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
