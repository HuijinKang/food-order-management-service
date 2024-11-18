package org.sparta.foodordermanagementservice.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.common.PageSizeRule;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.CreatePaymentDTO;
import org.sparta.foodordermanagementservice.dto.PaginatePaymentsDTO;
import org.sparta.foodordermanagementservice.dto.ReqPatchPayment;
import org.sparta.foodordermanagementservice.dto.UpdatePaymentDTO;
import org.sparta.foodordermanagementservice.dto.request.ReqPostPayment;
import org.sparta.foodordermanagementservice.dto.request.SortedBy;
import org.sparta.foodordermanagementservice.dto.response.ResPayment;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/payments")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{id}")
    public ApiResponse<ResPayment> readPayment(@PathVariable UUID id,
                                               @AuthenticationPrincipal UserDetails userDetails) {

        ResPayment resPayment
                = paymentService.readPayment(id, userDetails);

        return ApiResponse.ofSuccess(resPayment);
    }

    @GetMapping
    public ApiResponse<Page<ResPayment>> paginatePayments
            (
                    @RequestParam @NotBlank String username,
                    @RequestParam @Min(1) int pageSize,
                    @RequestParam @Min(0) int pageNumber,
                    @RequestParam SortedBy sortedBy,
                    @RequestParam boolean isAsc,
                    @RequestParam Boolean includingDeleted,
                    @AuthenticationPrincipal UserDetails userDetails
            ) {

        if (!paymentService.authorizeToRead(userDetails, username, includingDeleted != null ? includingDeleted : false)) {

            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        PaginatePaymentsDTO dto
                = PaginatePaymentsDTO.builder()
                .username(username)
                .pageSize(PageSizeRule.validate(pageSize)
                        ? pageSize
                        : PageSizeRule.DEFAULT_PAGE_SIZE)
                .pageNumber(pageNumber)
                .sortedBy(sortedBy)
                .isAsc(isAsc)
                .includingDeleted(includingDeleted == null
                        ? false
                        : includingDeleted)
                .pageRequest(PageRequest.of(pageNumber, pageSize))
                .build();

        Page<ResPayment> pagedPayments
                = paymentService.paginatePayments(dto);

        return ApiResponse.ofSuccess(pagedPayments);
    }

    @PostMapping
    @Secured(UserRole.Authority.MASTER)
    public ApiResponse<UUID> createPayment(@RequestBody ReqPostPayment request) {

        UUID createdPaymentId
                = paymentService.createPayment(CreatePaymentDTO.from(request));

        return ApiResponse.ofSuccess(createdPaymentId);
    }

    @PatchMapping("/{id}")
    @Secured(UserRole.Authority.MASTER)
    public ApiResponse<Void> updatePayment(@PathVariable UUID id,
                                           @RequestBody ReqPatchPayment request) {

        paymentService.updatePayment(id, UpdatePaymentDTO.from(request));

        return ApiResponse.ofSuccess(null);
    }


    @DeleteMapping("/{id}")
    @Secured(UserRole.Authority.MASTER)
    public ApiResponse<Void> deletePayment(@PathVariable UUID id) {

        //todo 권한 인증
        paymentService.deletePayment(id);

        return ApiResponse.ofSuccess(null);
    }
}

