package org.sparta.foodordermanagementservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.dto.request.MenuDescriptionGenerateRequestDTO;
import org.sparta.foodordermanagementservice.dto.response.MenuDescriptionGenerateResponseDTO;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.service.GoogleApiService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/google")
public class GoogleAPIController {

    private final GoogleApiService googleApiService;

    @PostMapping("/ai/menu/description")
    @Secured(UserRole.Authority.OWNER)
    public ApiResponse<MenuDescriptionGenerateResponseDTO> generateMenuDescription(@Valid @RequestBody MenuDescriptionGenerateRequestDTO request,
                                                                                   @AuthenticationPrincipal UserDetails userDetails) {
        MenuDescriptionGenerateResponseDTO response = googleApiService.getAIMenuDescription(request);

        googleApiService.saveRecord(userDetails.getUsername(), request, response);

        return ApiResponse.ofSuccess(response);
    }
}
