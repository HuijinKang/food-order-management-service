package org.sparta.foodordermanagementservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sparta.foodordermanagementservice.dto.request.ReviewRequestDto;
import org.sparta.foodordermanagementservice.dto.response.ReviewResponseDto;
import org.sparta.foodordermanagementservice.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
class ReviewControllerTest {

    @MockBean
    private ReviewService reviewService;

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
    @DisplayName("리뷰 작성 성공")
    @WithMockUser(username = "testUser", roles = {"CUSTOMER"})
    void createReviewSuccess() throws Exception {
        UUID storeId = UUID.randomUUID();
        ReviewRequestDto requestDto = new ReviewRequestDto(5, "맛있어요!");

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/reviews")
                        .queryParam("storeId", storeId.toString())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("create-review-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("storeId").description("가게 ID")
                        ),
                        requestFields(
                                fieldWithPath("rating").type(JsonFieldType.NUMBER).description("리뷰 평점 (1~5)"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("리뷰 내용")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터").optional(),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("리뷰 수정 성공")
    @WithMockUser(username = "testUser", roles = {"CUSTOMER"})
    void updateReviewSuccess() throws Exception {
        UUID reviewId = UUID.randomUUID();
        ReviewRequestDto requestDto = new ReviewRequestDto(5, "수정된 맛있어요!");

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/reviews/{reviewId}", reviewId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("update-review-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("reviewId").description("수정할 리뷰의 ID")
                        ),
                        requestFields(
                                fieldWithPath("rating").type(JsonFieldType.NUMBER).description("수정된 리뷰 평점 (1~5)"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정된 리뷰 내용")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터").optional(),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("리뷰 삭제 성공")
    @WithMockUser(username = "testUser", roles = {"CUSTOMER", "MASTER"})
    void deleteReviewSuccessCustomer() throws Exception {
        UUID reviewId = UUID.randomUUID();

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/reviews/{reviewId}", reviewId)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("delete-review-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("reviewId").description("삭제할 리뷰의 ID")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터").optional(),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("리뷰 단건 조회 성공")
    void getReviewSuccess() throws Exception {
        UUID reviewId = UUID.randomUUID();

        ReviewResponseDto responseDto = ReviewResponseDto.builder()
                .rating(5)
                .content("정말 맛있어요!")
                .build();

        when(reviewService.getReview(reviewId)).thenReturn(responseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/reviews/{reviewId}", reviewId)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-review-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("reviewId").description("조회할 리뷰의 ID")
                        ),
                        responseFields(
                                fieldWithPath("data.rating").type(JsonFieldType.NUMBER).description("리뷰 평점 (1~5)"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING).description("리뷰 내용"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
                        )
                ));
    }


}
