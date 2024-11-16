package org.sparta.foodordermanagementservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.dto.CreatePaymentDTO;
import org.sparta.foodordermanagementservice.dto.ReqPatchPayment;
import org.sparta.foodordermanagementservice.dto.UpdatePaymentDTO;
import org.sparta.foodordermanagementservice.dto.request.ReqPostPayment;
import org.sparta.foodordermanagementservice.dto.response.GetPaymentRes;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.service.PaymentService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor

@RestController
@RequestMapping("api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{id}")
    @Secured({UserRole.Authority.CUSTOMER, UserRole.Authority.MASTER})
    public ApiResponse<GetPaymentRes> readPayment(@PathVariable UUID id) {

        GetPaymentRes getPaymentRes
                = paymentService.readPayment(id);

        return ApiResponse.ofSuccess(getPaymentRes);
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

        paymentService.deletePayment(id);

        return ApiResponse.ofSuccess(null);
    }
}
