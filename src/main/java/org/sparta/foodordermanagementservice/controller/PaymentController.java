package org.sparta.foodordermanagementservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.dto.PaymentDTO;
import org.sparta.foodordermanagementservice.dto.PostPaymentDTO;
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
    public ApiResponse<GetPaymentRes> getPayment(@PathVariable UUID id) {

        // todo auth check customer, master
        PaymentDTO gottenPayment = paymentService.getPayment(id);

        return ApiResponse.ofSuccess(
                GetPaymentRes.from(gottenPayment)
        );
    }

    @PostMapping
    public ApiResponse<UUID> postPayment(@RequestBody PostPaymentReq request) {

        //todo 권한 인증
        PaymentDTO postedPayment
                = paymentService.postPayment(PostPaymentDTO.from(request));

        return ApiResponse.ofSuccess(
                postedPayment.getId()
        );


    }

}
