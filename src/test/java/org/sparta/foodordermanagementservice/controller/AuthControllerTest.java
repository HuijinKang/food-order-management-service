package org.sparta.foodordermanagementservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.request.LoginRequestDTO;
import org.sparta.foodordermanagementservice.dto.request.SignupRequestDTO;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.service.AuthService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class})
class AuthControllerTest {
    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(provider))
                .build();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signupSuccess() throws Exception {

        SignupRequestDTO request = SignupRequestDTO.builder()
                .username("testuser")
                .password("testpassword")
                .email("test@email.com")
                .isPublic(true)
                .nickname("testnickname")
                .userRole(UserRole.CUSTOMER)
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("signup-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN).description("정보공개여부"),
                                        fieldWithPath("userRole").type(JsonFieldType.STRING).description("유저권한")
                                )
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과데이터").optional(),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지")
                        )));

    }

    @Test
    @DisplayName("회원가입 실패 - 아이디에 특수문자 존재")
    void signupFailWhenUsernameHasSpecialCharacter() throws Exception {
        SignupRequestDTO request = SignupRequestDTO.builder()
                .username("!testuser")
                .password("testpassword")
                .email("test@email.com")
                .isPublic(true)
                .nickname("testnickname")
                .userRole(UserRole.CUSTOMER)
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("signup-fail-username-has-special-character",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN).description("정보공개여부"),
                                        fieldWithPath("userRole").type(JsonFieldType.STRING).description("유저권한")
                                )
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과데이터").optional(),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지")
                        )));

    }

    @Test
    @DisplayName("회원가입 실패 - 아이디에 대문자 존재")
    void signupFailWhenUsernameHasUppercaseLetters() throws Exception {
        SignupRequestDTO request = SignupRequestDTO.builder()
                .username("Testuser")
                .password("testpassword")
                .email("test@email.com")
                .isPublic(true)
                .nickname("testnickname")
                .userRole(UserRole.CUSTOMER)
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("signup-fail-username-has-uppercase-letters",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN).description("정보공개여부"),
                                        fieldWithPath("userRole").type(JsonFieldType.STRING).description("유저권한")
                                )
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과데이터").optional(),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지")
                        )));

    }

    @Test
    @DisplayName("회원가입 실패 - 아이디 길이 4자 미만")
    void signupFailWhenUsernameIsLessThanFourCharacters() throws Exception {
        SignupRequestDTO request = SignupRequestDTO.builder()
                .username("use")
                .password("testpassword")
                .email("test@email.com")
                .isPublic(true)
                .nickname("testnickname")
                .userRole(UserRole.CUSTOMER)
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("signup-fail-username-is-less-than-four-characters",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN).description("정보공개여부"),
                                        fieldWithPath("userRole").type(JsonFieldType.STRING).description("유저권한")
                                )
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과데이터").optional(),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지")
                        )));

    }

    @Test
    @DisplayName("회원가입 실패 - 아이디 길이 10자 초과")
    void signupFailWhenUsernameIsMoreThanTenCharacters() throws Exception {
        SignupRequestDTO request = SignupRequestDTO.builder()
                .username("testusernam")
                .password("testpassword")
                .email("test@email.com")
                .isPublic(true)
                .nickname("testnickname")
                .userRole(UserRole.CUSTOMER)
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("signup-fail-username-is-more-than-ten-characters",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN).description("정보공개여부"),
                                        fieldWithPath("userRole").type(JsonFieldType.STRING).description("유저권한")
                                )
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과데이터").optional(),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지")
                        )));

    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호에 허용되지 않은 문자 존재")
    void signupFailWhenPasswordHasInvalidCharacter() throws Exception {
        SignupRequestDTO request = SignupRequestDTO.builder()
                .username("testuser")
                .password("testpassword테")
                .email("test@email.com")
                .isPublic(true)
                .nickname("testnickname")
                .userRole(UserRole.CUSTOMER)
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("signup-fail-password-has-invalid-character",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN).description("정보공개여부"),
                                        fieldWithPath("userRole").type(JsonFieldType.STRING).description("유저권한")
                                )
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과데이터").optional(),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지")
                        )));

    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 길이 8자 미만")
    void signupFailWhenPasswordIsLessThanEightCharacters() throws Exception {
        SignupRequestDTO request = SignupRequestDTO.builder()
                .username("testuser")
                .password("testpas")
                .email("test@email.com")
                .isPublic(true)
                .nickname("testnickname")
                .userRole(UserRole.CUSTOMER)
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("signup-fail-password-is-less-than-eight-characters",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN).description("정보공개여부"),
                                        fieldWithPath("userRole").type(JsonFieldType.STRING).description("유저권한")
                                )
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과데이터").optional(),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지")
                        )));

    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 길이 15자 초과")
    void signupFailWhenPasswordIsMoreThanFifteenCharacters() throws Exception {
        SignupRequestDTO request = SignupRequestDTO.builder()
                .username("testuser")
                .password("testpasswordtest")
                .email("test@email.com")
                .isPublic(true)
                .nickname("testnickname")
                .userRole(UserRole.CUSTOMER)
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("signup-fail-password-is-more-than-fifteen-characters",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN).description("정보공개여부"),
                                        fieldWithPath("userRole").type(JsonFieldType.STRING).description("유저권한")
                                )
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과데이터").optional(),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지")
                        )));

    }

    @Test
    @DisplayName("회원가입 실패 - 잘못된 이메일 형식")
    void signupFailWhenInvalidEmailFormat() throws Exception {
        SignupRequestDTO request = SignupRequestDTO.builder()
                .username("testuser")
                .password("testpassword")
                .email("testemail.com")
                .isPublic(true)
                .nickname("testnickname")
                .userRole(UserRole.CUSTOMER)
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("signup-fail-invalid-email-format",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN).description("정보공개여부"),
                                        fieldWithPath("userRole").type(JsonFieldType.STRING).description("유저권한")
                                )
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과데이터").optional(),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지")
                        )));

    }

    @Test
    @DisplayName("회원가입 실패 - 아이디 중복")
    void signupFailWhenUsernameIsDuplicated() throws Exception {
        SignupRequestDTO request = SignupRequestDTO.builder()
                .username("testuser")
                .password("testpassword")
                .email("test@email.com")
                .nickname("testnickname")
                .isPublic(true)
                .userRole(UserRole.CUSTOMER)
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        // signup이 void 타입이라 doThrow 사용
        doThrow(new CustomException(ErrorCode.DUPLICATE_USERNAME)).when(authService).signup(any(SignupRequestDTO.class));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("signup-fail-username-is-duplicated",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN).description("정보공개여부"),
                                        fieldWithPath("userRole").type(JsonFieldType.STRING).description("유저권한")
                                )
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과데이터").optional(),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지")
                        )));

    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 중복")
    void signupFailWhenNicknameIsDuplicated() throws Exception {
        SignupRequestDTO request = SignupRequestDTO.builder()
                .username("testuser")
                .password("testpassword")
                .email("test@email.com")
                .nickname("testnickname")
                .isPublic(true)
                .userRole(UserRole.CUSTOMER)
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        // signup이 void 타입이라 doThrow 사용
        doThrow(new CustomException(ErrorCode.DUPLICATE_NICKNAME)).when(authService).signup(any(SignupRequestDTO.class));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("signup-fail-nickname-is-duplicated",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN).description("정보공개여부"),
                                        fieldWithPath("userRole").type(JsonFieldType.STRING).description("유저권한")
                                )
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과데이터").optional(),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지")
                        )));

    }

    @Test
    @DisplayName("회원가입 실패 - 잘못된 역할 요청")
    void signupFailWhenWrongRoleRequest() throws Exception {
        SignupRequestDTO request = SignupRequestDTO.builder()
                .username("testuser")
                .password("testpassword")
                .email("test@email.com")
                .nickname("testnickname")
                .isPublic(true)
                .userRole(UserRole.MASTER)
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("signup-fail-wrong-role-request",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN).description("정보공개여부"),
                                        fieldWithPath("userRole").type(JsonFieldType.STRING).description("유저권한")
                                )
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과데이터").optional(),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지")
                        )));

    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() throws  Exception {
        LoginRequestDTO request = LoginRequestDTO.builder()
                .username("testuser")
                .password("testpassword")
                .build();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auths/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("login-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                )
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과데이터"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지")
                        ).and(
                                fieldWithPath("data.JWTToken").type(JsonFieldType.STRING).description("jwt 토큰"),
                                fieldWithPath("data.user").type(JsonFieldType.OBJECT).description("유저 정보")
                                ).and(
                                    fieldWithPath("data.user.username").type(JsonFieldType.STRING).description("사용자아이디"),
                                    fieldWithPath("data.user.nickname").type(JsonFieldType.STRING).description("닉네임"),
                                    fieldWithPath("data.user.email").type(JsonFieldType.STRING).description("이메일"),
                                    fieldWithPath("data.user.isPublic").type(JsonFieldType.BOOLEAN).description("정보공개 여부"),
                                    fieldWithPath("data.user.userRole").type(JsonFieldType.STRING).description("사용자 역할")

                        )));
    }

    @Test
    @DisplayName("로그인 실패 - 없는 사용자")
    void loginFailWhenUserNotFound() throws  Exception {
        LoginRequestDTO request = LoginRequestDTO.builder()
                .username("nottestuser")
                .password("testpassword")
                .build();
        String requestJson = objectMapper.writeValueAsString(request);

        when(authService.login(any(LoginRequestDTO.class))).thenThrow(new CustomException(ErrorCode.FAIL_LOGIN));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auths/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is(ErrorCode.FAIL_LOGIN.getStatus().value()))
                .andDo(print())
                .andDo(document("login-fail-user-not-found",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                )
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과데이터"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지")
                        )));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void loginFailWhenPasswordIsIncorrect() throws  Exception {
        LoginRequestDTO request = LoginRequestDTO.builder()
                .username("testuser")
                .password("nottestpassword")
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        when(authService.login(any(LoginRequestDTO.class))).thenThrow(new CustomException(ErrorCode.FAIL_LOGIN));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auths/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is(ErrorCode.FAIL_LOGIN.getStatus().value()))
                .andDo(print())
                .andDo(document("login-fail-password-is-incorrect",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                )
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과데이터"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지")
                        )));
    }
}