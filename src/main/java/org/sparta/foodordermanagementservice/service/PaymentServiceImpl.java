package org.sparta.foodordermanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.common.utils.RoleUtils;
import org.sparta.foodordermanagementservice.dto.CreatePaymentDTO;
import org.sparta.foodordermanagementservice.dto.PaginatePaymentsDTO;
import org.sparta.foodordermanagementservice.dto.PaymentDTO;
import org.sparta.foodordermanagementservice.dto.UpdatePaymentDTO;
import org.sparta.foodordermanagementservice.dto.response.ResPayment;
import org.sparta.foodordermanagementservice.entity.Payment;
import org.sparta.foodordermanagementservice.repository.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;


    @Override
    public ResPayment readPayment(UUID paymentId, UserDetails userDetails) {

        PaymentDTO foundPayment
                = PaymentDTO.from(repository.readPayment(paymentId));

        if (!authorizeToRead
                (userDetails, foundPayment.getUsername(), false)) {

            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        return ResPayment.from(foundPayment);
    }

    public boolean authorizeToRead(UserDetails userDetails, String paymentUserName, Boolean includingDeleted) {

        boolean userAuthorized
                = paymentUserName.equals(userDetails.getUsername())
                && (includingDeleted == null || !includingDeleted);

        return userAuthorized || RoleUtils.hasMasterRole(userDetails);
    }

    @Override
    public UUID createPayment(CreatePaymentDTO dto) {

        Payment createdPayment
                = repository.createPayment(dto);

        return createdPayment.getId();
    }

    @Override
    public void updatePayment(UUID id, UpdatePaymentDTO dto) {

        repository.updatePayment(id, dto);
    }

    @Override
    public void deletePayment(UUID paymentId) {

        String deleter = "admin";/*todo auth완료시 username으로 넣기 */

        repository.deletePayment(paymentId, deleter);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResPayment> paginatePayments(PaginatePaymentsDTO dto) {


        List<ResPayment> pageContent
                = repository.readCurrentPagePayments(dto)
                .stream()
                .map(ResPayment::from)
                .toList();

        long totalPayments
                = repository.countTotalPayments(dto.getUsername(), dto.isIncludingDeleted());

        return new PageImpl<>(pageContent, dto.getPageRequest(), totalPayments);

    }
}
