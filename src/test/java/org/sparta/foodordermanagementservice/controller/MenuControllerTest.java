package org.sparta.foodordermanagementservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sparta.foodordermanagementservice.dto.request.MenuRequestDto;
import org.sparta.foodordermanagementservice.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.UUID;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
class MenuControllerTest {

    @MockBean
    private MenuService menuService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(provider))
                .build();
    }

    @Test
    @DisplayName("메뉴 등록 성공")
    @WithMockUser(username = "testUser", roles = {"OWNER", "MASTER"})
    void createMenuSuccessWithoutFile() throws Exception {

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/menus")
                        .queryParam("storeId", UUID.randomUUID().toString())
                        .queryParam("name", "테스트 메뉴")
                        .queryParam("price", "10000")
                        .queryParam("description", "테스트 메뉴 설명")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("create-menu-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("storeId").description("가게 ID"),
                                parameterWithName("name").description("메뉴 이름"),
                                parameterWithName("price").description("메뉴 가격"),
                                parameterWithName("description").description("메뉴 설명")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터").optional(),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
                        )
                ));

    }


    @Test
    @DisplayName("메뉴 수정 성공")
    @WithMockUser(username = "testUser", roles = {"OWNER", "MASTER"})
    void updateMenuSuccessWithoutFile() throws Exception {
        UUID menuId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/menus/{menuId}", menuId)
                        .queryParam("storeId", storeId.toString())
                        .queryParam("name", "수정된 메뉴")
                        .queryParam("price", "15000")
                        .queryParam("description", "수정된 메뉴 설명")
                        .queryParam("status", "ACTIVE")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("update-menu-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("storeId").description("가게 ID"),
                                parameterWithName("name").description("수정된 메뉴 이름"),
                                parameterWithName("price").description("수정된 메뉴 가격"),
                                parameterWithName("description").description("수정된 메뉴 설명"),
                                parameterWithName("status").description("수정된 메뉴 상태")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터").optional(),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
                        )
                ));
    }





}
