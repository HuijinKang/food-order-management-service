package org.sparta.foodordermanagementservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sparta.foodordermanagementservice.dto.request.MenuDescriptionGenerateRequestDTO;
import org.sparta.foodordermanagementservice.dto.response.MenuDescriptionGenerateResponseDTO;
import org.sparta.foodordermanagementservice.service.GoogleApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class})
class GoogleAPIControllerTest {
    @MockBean
    private GoogleApiService googleApiService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(provider))
                .build();
    }

    @Test
    @DisplayName("메뉴 설명 생성 성공")
    @WithMockUser(username = "testUser", roles = {"OWNER"})
    void generateMenuDescriptionSuccess() throws Exception {
        MenuDescriptionGenerateRequestDTO request = MenuDescriptionGenerateRequestDTO.builder()
                .question("질문")
                .build();
        MenuDescriptionGenerateResponseDTO response = MenuDescriptionGenerateResponseDTO.builder()
                .answer("답변")
                .build();

        when(googleApiService.getAIMenuDescription(any(MenuDescriptionGenerateRequestDTO.class)))
                .thenReturn(response);

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/google/ai/menu/description")
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-generate-menu-description-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(fieldWithPath("question").type(JsonFieldType.STRING).description("요청문")),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과데이터")
                        ).and(
                                fieldWithPath("data.answer").type(JsonFieldType.STRING).description("응답문")
                        )));
    }

    @Test
    @DisplayName("메뉴 설명 생성 실패 - 가게 주인만 가능")
    @WithMockUser(username = "testUser", roles = {"CUSTOMER"})
    void generateMenuDescriptionFailWhenForbidden() throws Exception {
        MenuDescriptionGenerateRequestDTO request = MenuDescriptionGenerateRequestDTO.builder()
                .question("질문")
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/google/ai/menu/description")
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andDo(document("post-generate-menu-description-fail-forbidden",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(fieldWithPath("question").type(JsonFieldType.STRING).description("요청문")),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과데이터")
                        )));
    }
}