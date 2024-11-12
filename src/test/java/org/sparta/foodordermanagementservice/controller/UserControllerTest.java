package org.sparta.foodordermanagementservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.UserDTO;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.entity.UserStatus;
import org.sparta.foodordermanagementservice.service.UserService;
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

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class})
class UserControllerTest {
    @MockBean
    private UserService userService;

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
    @WithMockUser(username = "testuser", roles = {"MANAGER", "MASTER"})
    @DisplayName("유저 조회 성공")
    void getUserSuccess() throws Exception {
        UserDTO testUser = UserDTO.builder()
                .username("testuser")
                .password("testpassword")
                .email("test@test.com")
                .userRole(UserRole.MASTER)
                .status(UserStatus.ACTIVE)
                .isPublic(true)
                .nickname("testnickname")
                .build();

        when(userService.getUser(anyString())).thenReturn(testUser);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/users/{username}", testUser.getUsername())
                        .header("Authorization", "Bearer {ACCESS_TOKEN}"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-user-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과데이터")
                        ).and(
                                fieldWithPath("data.username").type(JsonFieldType.STRING).description("사용자아이디"),
                                fieldWithPath("data.password").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("data.status").type(JsonFieldType.STRING).description("상태"),
                                fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("data.isPublic").type(JsonFieldType.BOOLEAN).description("정보공개 여부"),
                                fieldWithPath("data.userRole").type(JsonFieldType.STRING).description("사용자 역할")
                        )));
    }

    @Test
    @DisplayName("유저 조회 실패 - 접근 권한 없음")
    @WithMockUser(username = "testuser", roles = {"CUSTOMER", "OWNER"})
    void getUserFailWhenUnauthorized() throws Exception {
        UserDTO notMasterUser = UserDTO.builder()
                .username("testuser")
                .password("testpassword")
                .email("test@test.com")
                .userRole(UserRole.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .isPublic(true)
                .nickname("testnickname")
                .build();

        when(userService.getUser(anyString())).thenThrow(new CustomException(ErrorCode.UNAUTHORIZED));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/users/{username}", notMasterUser.getUsername())
                        .header("Authorization", "Bearer {ACCESS_TOKEN}"))
                .andExpect(status().is(ErrorCode.UNAUTHORIZED.getStatus().value()))
                .andDo(print())
                .andDo(document("get-user-fail-unauthorized",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과데이터")
                        )));
    }

    @Test
    @DisplayName("유저 조회 실패 - 없는 사용자")
    @WithMockUser(username = "testuser", roles = {"MANAGER", "MASTER"})
    void getUserFailWhenUserNotFound() throws Exception {

        when(userService.getUser(anyString())).thenThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/users/{username}", "notExistingUser")
                        .header("Authorization", "Bearer {ACCESS_TOKEN}"))
                .andExpect(status().is(ErrorCode.USER_NOT_FOUND.getStatus().value()))
                .andDo(print())
                .andDo(document("get-user-fail-user-not-found",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과데이터")
                        )));
    }
}