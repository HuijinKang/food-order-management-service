package org.sparta.foodordermanagementservice.common.exeption;


import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

}