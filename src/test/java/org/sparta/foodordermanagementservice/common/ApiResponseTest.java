package org.sparta.foodordermanagementservice.common;

import org.junit.jupiter.api.Test;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void ofError() {
        ApiResponse<Void> apiResponse = ApiResponse.ofError(ErrorCode.BAD_REQUEST);
        assertNull(apiResponse.data());
        assertEquals("잘못된 요청입니다.", apiResponse.message());
        assertEquals("C011", apiResponse.code());
    }

    @Test
    void ofSuccess() {
        ApiResponse<String> apiResponse = ApiResponse.ofSuccess("data");
        assertEquals("data", apiResponse.data());
        assertEquals("OK", apiResponse.message());
        assertEquals("S001", apiResponse.code());
    }

}