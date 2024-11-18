package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.request.MenuDescriptionGenerateRequestDTO;
import org.sparta.foodordermanagementservice.dto.response.MenuDescriptionGenerateResponseDTO;

public interface GoogleApiService {
    MenuDescriptionGenerateResponseDTO getAIMenuDescription(MenuDescriptionGenerateRequestDTO request);

    void saveRecord(String username, MenuDescriptionGenerateRequestDTO request, MenuDescriptionGenerateResponseDTO response);
}
