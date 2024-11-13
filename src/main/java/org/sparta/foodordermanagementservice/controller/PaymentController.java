package org.sparta.foodordermanagementservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.dto.CreatePaymentDTO;
import org.sparta.foodordermanagementservice.dto.request.PostPaymentReq;
import org.sparta.foodordermanagementservice.dto.response.GetPaymentRes;
import org.sparta.foodordermanagementservice.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor

@RestController
@RequestMapping("api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{id}")
    public ApiResponse<GetPaymentRes> readPayment(@PathVariable UUID id) {

        // todo auth check customer, master
        GetPaymentRes getPaymentRes
                = paymentService.readPayment(id);

        return ApiResponse.ofSuccess(getPaymentRes);
    }

    @PostMapping
    public ApiResponse<UUID> createPayment(@RequestBody PostPaymentReq request) {

        //todo 권한 인증
        UUID createdPaymentId
                = paymentService.createPayment(CreatePaymentDTO.from(request));

        return ApiResponse.ofSuccess(createdPaymentId);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePayment(@PathVariable UUID id) {

        //todo 권한 인증
        paymentService.deletePayment(id);

        return ApiResponse.ofSuccess(null);
    }
}
