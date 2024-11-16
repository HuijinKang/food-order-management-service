package org.sparta.foodordermanagementservice.service.Impl;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.request.GeminiRequestDTO;
import org.sparta.foodordermanagementservice.dto.request.MenuDescriptionGenerateRequestDTO;
import org.sparta.foodordermanagementservice.dto.response.GeminiResponseDTO;
import org.sparta.foodordermanagementservice.dto.response.MenuDescriptionGenerateResponseDTO;
import org.sparta.foodordermanagementservice.entity.AiApiLog;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.repository.AiApiLogRepository;
import org.sparta.foodordermanagementservice.repository.UserRepository;
import org.sparta.foodordermanagementservice.service.GoogleApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoogleApiServiceImpl implements GoogleApiService {
    private final AiApiLogRepository aiApiLogRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${google.api.key}")
    private String apiKey;

    @Override
    public MenuDescriptionGenerateResponseDTO getAIMenuDescription(MenuDescriptionGenerateRequestDTO request) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + apiKey;
        String postfix = "답변을 최대한 간결하게 50자 이하로";

        GeminiRequestDTO geminiRequest = GeminiRequestDTO.builder()
                .question(request.getQuestion() + postfix)
                .build();

        try {
            GeminiResponseDTO response = restTemplate.postForObject(url, geminiRequest, GeminiResponseDTO.class);
            String message = Objects.requireNonNull(response).getCandidates().get(0).getContent().getParts().get(0).getText();
            System.out.println("!!!!");
            System.out.println(message);
            return MenuDescriptionGenerateResponseDTO.builder()
                    .answer(message)
                    .build();

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Transactional
    @Override
    public void saveRecord(String username, MenuDescriptionGenerateRequestDTO request, MenuDescriptionGenerateResponseDTO response) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        AiApiLog record = AiApiLog.builder()
                .user(user)
                .question(request.getQuestion())
                .answer(response.getAnswer())
                .createdBy(user.getUsername())
                .updatedBy(user.getUsername())
                .build();

        aiApiLogRepository.save(record);
    }


}
