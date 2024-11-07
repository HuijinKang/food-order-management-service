package org.sparta.foodordermanagementservice.common;


import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;

public record ApiResponse<T>(
	T data,
	String message,
	String code
) {

	public static ApiResponse<Void> ofError(ErrorCode code) {
		return new ApiResponse<>(null, code.getDescription(), code.getCode());
	}


	public static <T> ApiResponse<T> ofSuccess(T data) {
		return new ApiResponse<>(data, "OK", "S001");
	}

}