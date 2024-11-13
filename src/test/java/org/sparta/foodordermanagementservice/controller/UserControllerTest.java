package org.sparta.foodordermanagementservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.UserDTO;
import org.sparta.foodordermanagementservice.dto.request.UpdateUserRoleRequestDTO;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.entity.UserStatus;
import org.sparta.foodordermanagementservice.service.UserService;
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

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
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
class UserControllerTest {
    @MockBean
    private UserService userService;

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
    @WithMockUser(username = "testUser", roles = {"MANAGER", "MASTER"})
    @DisplayName("유저 조회 성공")
    void getUserSuccess() throws Exception {
        UserDTO testUser = UserDTO.builder()
                .username("testUser")
                .email("test@test.com")
                .userRole(UserRole.MASTER)
                .status(UserStatus.ACTIVE)
                .isPublic(true)
                .nickname("testNickname")
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
                                fieldWithPath("data.status").type(JsonFieldType.STRING).description("상태"),
                                fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("data.isPublic").type(JsonFieldType.BOOLEAN).description("정보공개 여부"),
                                fieldWithPath("data.userRole").type(JsonFieldType.STRING).description("사용자 역할")
                        )));
    }

    @Test
    @DisplayName("유저 조회 실패 - 접근 권한 없음")
    @WithMockUser(username = "testUser", roles = {"CUSTOMER", "OWNER"})
    void getUserFailWhenForbidden() throws Exception {

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/users/{username}", "notMasterOrManagerUsername")
                        .header("Authorization", "Bearer {ACCESS_TOKEN}"))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andDo(document("get-user-fail-forbidden",
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
    @WithMockUser(username = "testUser", roles = {"MANAGER", "MASTER"})
    void getUserFailWhenUserNotFound() throws Exception {

        when(userService.getUser(anyString())).thenThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/users/{username}", "notExistingUsername")
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

    @Test
    @DisplayName("유저 권한 수정 성공")
    @WithMockUser(username = "master", roles = {"MASTER"})
    void updateUserRoleSuccess() throws Exception {
        UpdateUserRoleRequestDTO request = UpdateUserRoleRequestDTO.builder()
                .userRole(UserRole.MANAGER)
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/users/role/{username}", "testUsername")
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("patch-user-role-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userRole").type(JsonFieldType.STRING).description("변경할 사용자 역할")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과데이터")
                        )));
    }

    @Test
    @WithMockUser(username = "master", roles = {"MASTER"})
    @DisplayName("유저 권한 수정 실패 - 없는 사용자")
    void updateUserRoleFailWhenUserNotFound() throws Exception {
        UpdateUserRoleRequestDTO request = UpdateUserRoleRequestDTO.builder()
                .userRole(UserRole.MANAGER)
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        doThrow(new CustomException(ErrorCode.USER_NOT_FOUND)).when(userService).updateUserRole(anyString(), any(UserRole.class));

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/users/role/{username}", "notExistingUsername")
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("patch-user-role-fail-user-not-found",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userRole").type(JsonFieldType.STRING).description("변경할 사용자 역할")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과데이터")
                        )));
    }

    @Test
    @WithMockUser(username = "notMaster", roles = {"MANAGER", "OWNER", "CUSTOMER"})
    @DisplayName("유저 권한 수정 실패 - 마스터가 아닌 사용자가 권한 수정")
    void updateUserRoleFailWhenForbidden() throws Exception {
        UpdateUserRoleRequestDTO request = UpdateUserRoleRequestDTO.builder()
                .userRole(UserRole.MANAGER)
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/users/role/{username}", "testUsername")
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andDo(document("patch-user-role-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userRole").type(JsonFieldType.STRING).description("변경할 사용자 역할")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과데이터")
                        )));
    }
}