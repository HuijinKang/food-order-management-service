package org.sparta.foodordermanagementservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.request.*;
import org.sparta.foodordermanagementservice.dto.response.CategoryStoreListDTO;
import org.sparta.foodordermanagementservice.entity.*;
import org.sparta.foodordermanagementservice.security.UserDetailsImpl;
import org.sparta.foodordermanagementservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.mockito.BDDMockito.given;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class})
public class CategoryControllerTest {
    @MockBean
    private CategoryService categoryService;

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
    @DisplayName("카테고리 단건 조회 성공")
    @WithMockUser(roles = {"MASTER"})
    public void getCategoryById() throws Exception {
        UUID categoryId = UUID.randomUUID();
        Category category = Category.builder()
                .id(categoryId)
                .name("Beverages")
                .deletedAt(null)
                .deletedBy(null)
                .build();

        given(categoryService.getCategoryById(categoryId)).willReturn(category);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/category/{categoryId}", categoryId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-category-by-id",
                        pathParameters(
                                parameterWithName("categoryId").description("조회할 카테고리의 UUID")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과 데이터 (카테고리 정보, null일 수 있음)"),
                                fieldWithPath("data.id").type(JsonFieldType.STRING).optional().description("카테고리 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).optional().description("카테고리 이름"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING).optional().description("생성 일시"),
                                fieldWithPath("data.createdBy").type(JsonFieldType.STRING).optional().description("생성자"),
                                fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).optional().description("수정 일시"),
                                fieldWithPath("data.updatedBy").type(JsonFieldType.STRING).optional().description("수정자"),
                                fieldWithPath("data.deletedAt").type(JsonFieldType.STRING).optional().description("삭제 일시 (null일 경우 삭제되지 않음)"),
                                fieldWithPath("data.deletedBy").type(JsonFieldType.STRING).optional().description("삭제자 (null일 경우 삭제되지 않음)"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드")
                        )
                ));
    }

    @Test
    @DisplayName("카테고리 단건 조회 실패 - 존재하지 않는 카테고리")
    @WithMockUser(roles = {"MASTER"})
    public void getCategoryById_NotFound() throws Exception {
        UUID categoryId = UUID.randomUUID();

        when(categoryService.getCategoryById(any(UUID.class))).thenThrow(new CustomException(ErrorCode.NOT_FOUND_RESOURCE));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/category/{categoryId}", categoryId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("get-category-by-id-not-found",
                        pathParameters(
                                parameterWithName("categoryId").description("조회할 카테고리의 UUID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과데이터")
                        )
                ));
    }

    @Test
    @DisplayName("카테고리 단건 조회 실패 - 접근 권한 없음")
    @WithMockUser(roles = {"CUSTOMER", "OWNER"})
    public void getCategoryById_Forbidden() throws Exception {
        UUID categoryId = UUID.randomUUID();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/category/{categoryId}", categoryId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(document("get-category-by-id-forbidden",
                        pathParameters(
                                parameterWithName("categoryId").description("조회할 카테고리의 UUID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과데이터")
                        )
                ));
    }

    @Test
    @DisplayName("카테고리명으로 가게 조회 성공")
    @WithMockUser(roles = {"MASTER"})
    public void getStoresByCategory() throws Exception {
        String categoryName = "Beverages";
        List<CategoryStoreListDTO> stores = List.of(
                CategoryStoreListDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Store 1")
                        .region("Region 1")
                        .latitude(37.5665)
                        .longitude(126.9780)
                        .createdAt(LocalDateTime.now())
                        .createdBy("admin")
                        .updatedAt(LocalDateTime.now())
                        .updatedBy("admin")
                        .build(),
                CategoryStoreListDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Store 2")
                        .region("Region 2")
                        .latitude(37.5665)
                        .longitude(126.9780)
                        .createdAt(LocalDateTime.now())
                        .createdBy("admin")
                        .updatedAt(LocalDateTime.now())
                        .updatedBy("admin")
                        .build()
        );

        given(categoryService.getStoresByCategory(categoryName)).willReturn(stores);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/category/{categoryName}/stores", categoryName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-stores-by-category",
                        pathParameters(
                                parameterWithName("categoryName").description("조회할 카테고리의 이름")
                        ),
                        responseFields(
                                fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("가게 목록"),
                                fieldWithPath("data[].id").type(JsonFieldType.STRING).description("가게 ID"),
                                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("가게 이름"),
                                fieldWithPath("data[].region").type(JsonFieldType.STRING).description("가게 지역"),
                                fieldWithPath("data[].latitude").type(JsonFieldType.NUMBER).description("가게 위도"),
                                fieldWithPath("data[].longitude").type(JsonFieldType.NUMBER).description("가게 경도"),
                                fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("가게 생성 일시"),
                                fieldWithPath("data[].createdBy").type(JsonFieldType.STRING).description("가게 생성자"),
                                fieldWithPath("data[].updatedAt").type(JsonFieldType.STRING).description("가게 수정 일시"),
                                fieldWithPath("data[].updatedBy").type(JsonFieldType.STRING).description("가게 수정자"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드")
                        )
                ));
    }

    @Test
    @DisplayName("카테고리명으로 가게 조회 실패 - 존재하지 않는 카테고리")
    @WithMockUser(roles = {"MASTER"})
    public void getStoresByCategory_NotFound() throws Exception {
        String categoryName = "NonExistentCategory";

        when(categoryService.getStoresByCategory(categoryName)).thenThrow(new CustomException(ErrorCode.NOT_FOUND_RESOURCE));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/category/{categoryName}/stores", categoryName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("get-stores-by-category-not-found",
                        pathParameters(
                                parameterWithName("categoryName").description("조회할 카테고리의 이름")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과데이터")
                        )
                ));
    }

    @Test
    @DisplayName("카테고리 등록 성공")
    @WithMockUser(roles = {"MASTER"})
    public void registerCategory() throws Exception {
        User testUser = User.builder()
                .username("testUser")
                .email("test@test.com")
                .userRole(UserRole.MASTER)
                .status(UserStatus.ACTIVE)
                .isPublic(true)
                .nickname("testNickname")
                .build();

        UserDetailsImpl userDetails = new UserDetailsImpl(testUser);

        // SecurityContext에 인증 객체 설정
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );

        CategoryRegistrationRequestDTO registrationRequestDTO = new CategoryRegistrationRequestDTO();
        registrationRequestDTO.setName("Beverages");

        Category category = Category.builder()
                .name(registrationRequestDTO.getName())
                .build();

        given(categoryService.registerCategory(any(CategoryRegistrationRequestDTO.class), eq(testUser))).willReturn(category);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequestDTO)))
                .andExpect(status().isOk())
                .andDo(document("register-category",
                        requestFields(
                                fieldWithPath("name").description("등록할 카테고리 이름")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과 데이터 (카테고리 정보, null일 수 있음)"),
                                fieldWithPath("data.id").type(JsonFieldType.STRING).optional().description("카테고리 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).optional().description("카테고리 이름"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING).optional().description("생성 일시"),
                                fieldWithPath("data.createdBy").type(JsonFieldType.STRING).optional().description("생성자"),
                                fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).optional().description("수정 일시"),
                                fieldWithPath("data.updatedBy").type(JsonFieldType.STRING).optional().description("수정자"),
                                fieldWithPath("data.deletedAt").type(JsonFieldType.STRING).optional().description("삭제 일시 (null일 경우 삭제되지 않음)"),
                                fieldWithPath("data.deletedBy").type(JsonFieldType.STRING).optional().description("삭제자 (null일 경우 삭제되지 않음)"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드")
                        )
                ));
    }

    @Test
    @DisplayName("카테고리 등록 실패 - 접근 권한 없음")
    @WithMockUser(roles = {"OWNER"})
    public void registerCategory_Forbidden() throws Exception {
        CategoryRegistrationRequestDTO registrationRequestDTO = new CategoryRegistrationRequestDTO();
        registrationRequestDTO.setName("Beverages");

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequestDTO)))
                .andExpect(status().isForbidden())
                .andDo(document("register-category-forbidden",
                        requestFields(
                                fieldWithPath("name").description("등록할 카테고리 이름")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과데이터")
                        )
                ));
    }

    @Test
    @DisplayName("카테고리 수정 성공")
    @WithMockUser(roles = {"MASTER"})
    public void updateCategory_Success() throws Exception {
        User testUser = User.builder()
                .username("testUser")
                .email("test@test.com")
                .userRole(UserRole.MASTER)
                .status(UserStatus.ACTIVE)
                .isPublic(true)
                .nickname("testNickname")
                .build();

        UserDetailsImpl userDetails = new UserDetailsImpl(testUser);

        // SecurityContext에 인증 객체 설정
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );

        UUID categoryId = UUID.randomUUID();
        CategoryUpdateRequestDTO updateRequestDTO = new CategoryUpdateRequestDTO();
        updateRequestDTO.setName("Updated Beverages");

        Category updatedCategory = Category.builder()
                .id(categoryId)
                .name(updateRequestDTO.getName())
                .build();

        given(categoryService.updateCategory(eq(categoryId), any(CategoryUpdateRequestDTO.class), eq(testUser))).willReturn(updatedCategory);

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/category/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isOk())
                .andDo(document("update-category",
                        pathParameters(
                                parameterWithName("categoryId").description("수정할 카테고리의 UUID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("수정할 카테고리 이름")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과 데이터 (수정된 카테고리 정보, null일 수 있음)"),
                                fieldWithPath("data.id").type(JsonFieldType.STRING).optional().description("카테고리 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).optional().description("수정된 카테고리 이름"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING).optional().description("생성 일시"),
                                fieldWithPath("data.createdBy").type(JsonFieldType.STRING).optional().description("생성자"),
                                fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).optional().description("수정 일시"),
                                fieldWithPath("data.updatedBy").type(JsonFieldType.STRING).optional().description("수정자"),
                                fieldWithPath("data.deletedAt").type(JsonFieldType.STRING).optional().description("삭제 일시 (null일 경우 삭제되지 않음)"),
                                fieldWithPath("data.deletedBy").type(JsonFieldType.STRING).optional().description("삭제자 (null일 경우 삭제되지 않음)"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드")
                        )
                ));
    }

    @Test
    @DisplayName("카테고리 수정 실패 - 존재하지 않는 카테고리")
    @WithMockUser(roles = {"MASTER"})
    public void updateCategory_NotFound() throws Exception {
        User testUser = User.builder()
                .username("testUser")
                .email("test@test.com")
                .userRole(UserRole.MASTER)
                .status(UserStatus.ACTIVE)
                .isPublic(true)
                .nickname("testNickname")
                .build();

        UserDetailsImpl userDetails = new UserDetailsImpl(testUser);

        // SecurityContext에 인증 객체 설정
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );
        UUID categoryId = UUID.randomUUID();
        CategoryUpdateRequestDTO updateRequestDTO = new CategoryUpdateRequestDTO();
        updateRequestDTO.setName("Updated Beverages");

        given(categoryService.updateCategory(eq(categoryId), any(CategoryUpdateRequestDTO.class), eq(testUser)))
                .willThrow(new CustomException(ErrorCode.NOT_FOUND_RESOURCE));

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/category/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isNotFound())
                .andDo(document("update-category-not-found",
                        pathParameters(
                                parameterWithName("categoryId").description("수정할 카테고리의 UUID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("수정할 카테고리 이름")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과 데이터 (수정된 카테고리 정보, null일 수 있음)"),
                                fieldWithPath("data.id").type(JsonFieldType.STRING).optional().description("카테고리 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).optional().description("수정된 카테고리 이름"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING).optional().description("생성 일시"),
                                fieldWithPath("data.createdBy").type(JsonFieldType.STRING).optional().description("생성자"),
                                fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).optional().description("수정 일시"),
                                fieldWithPath("data.updatedBy").type(JsonFieldType.STRING).optional().description("수정자"),
                                fieldWithPath("data.deletedAt").type(JsonFieldType.STRING).optional().description("삭제 일시 (null일 경우 삭제되지 않음)"),
                                fieldWithPath("data.deletedBy").type(JsonFieldType.STRING).optional().description("삭제자 (null일 경우 삭제되지 않음)"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드")
                        )
                ));
    }

    @Test
    @DisplayName("카테고리 수정 실패 - 접근 권한 없음")
    @WithMockUser(roles = {"CUSTOMER"})
    public void updateCategory_Forbidden() throws Exception {
        UUID categoryId = UUID.randomUUID();
        CategoryUpdateRequestDTO updateRequestDTO = new CategoryUpdateRequestDTO();
        updateRequestDTO.setName("Updated Beverages");

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/category/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isForbidden())
                .andDo(document("update-category-forbidden",
                        pathParameters(
                                parameterWithName("categoryId").description("수정할 카테고리의 UUID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("수정할 카테고리 이름")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과 데이터 (수정된 카테고리 정보, null일 수 있음)"),
                                fieldWithPath("data.id").type(JsonFieldType.STRING).optional().description("카테고리 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).optional().description("수정된 카테고리 이름"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING).optional().description("생성 일시"),
                                fieldWithPath("data.createdBy").type(JsonFieldType.STRING).optional().description("생성자"),
                                fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).optional().description("수정 일시"),
                                fieldWithPath("data.updatedBy").type(JsonFieldType.STRING).optional().description("수정자"),
                                fieldWithPath("data.deletedAt").type(JsonFieldType.STRING).optional().description("삭제 일시 (null일 경우 삭제되지 않음)"),
                                fieldWithPath("data.deletedBy").type(JsonFieldType.STRING).optional().description("삭제자 (null일 경우 삭제되지 않음)"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드")
                        )
                ));
    }

    @Test
    @DisplayName("카테고리 삭제 성공")
    @WithMockUser(roles = {"MASTER"})
    public void deleteCategory_Success() throws Exception {
        UUID categoryId = UUID.randomUUID();
        Category deletedCategory = Category.builder()
                .id(categoryId)
                .name("Beverages")
                .deletedAt(LocalDateTime.now())
                .deletedBy("admin")
                .build();

        given(categoryService.deleteCategory(eq(categoryId), any(UserDetails.class))).willReturn(deletedCategory);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/category/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("delete-category",
                        pathParameters(
                                parameterWithName("categoryId").description("삭제할 카테고리의 UUID")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과 데이터 (삭제된 카테고리 정보, null일 수 있음)"),
                                fieldWithPath("data.id").type(JsonFieldType.STRING).optional().description("카테고리 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).optional().description("카테고리 이름"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING).optional().description("생성 일시"),
                                fieldWithPath("data.createdBy").type(JsonFieldType.STRING).optional().description("생성자"),
                                fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).optional().description("수정 일시"),
                                fieldWithPath("data.updatedBy").type(JsonFieldType.STRING).optional().description("수정자"),
                                fieldWithPath("data.deletedAt").type(JsonFieldType.STRING).optional().description("삭제 일시"),
                                fieldWithPath("data.deletedBy").type(JsonFieldType.STRING).optional().description("삭제자"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드")
                        )
                ));
    }

    @Test
    @DisplayName("카테고리 삭제 실패 - 존재하지 않는 카테고리")
    @WithMockUser(roles = {"MASTER"})
    public void deleteCategory_NotFound() throws Exception {
        UUID categoryId = UUID.randomUUID();

        given(categoryService.deleteCategory(eq(categoryId), any(UserDetails.class)))
                .willThrow(new CustomException(ErrorCode.NOT_FOUND_RESOURCE));

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/category/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("delete-category-not-found",
                        pathParameters(
                                parameterWithName("categoryId").description("삭제할 카테고리의 UUID")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과 데이터 (삭제된 카테고리 정보, null일 수 있음)"),
                                fieldWithPath("data.id").type(JsonFieldType.STRING).optional().description("카테고리 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).optional().description("카테고리 이름"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING).optional().description("생성 일시"),
                                fieldWithPath("data.createdBy").type(JsonFieldType.STRING).optional().description("생성자"),
                                fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).optional().description("수정 일시"),
                                fieldWithPath("data.updatedBy").type(JsonFieldType.STRING).optional().description("수정자"),
                                fieldWithPath("data.deletedAt").type(JsonFieldType.STRING).optional().description("삭제 일시"),
                                fieldWithPath("data.deletedBy").type(JsonFieldType.STRING).optional().description("삭제자"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드")
                        )
                ));
    }

    @Test
    @DisplayName("카테고리 삭제 실패 - 접근 권한 없음")
    @WithMockUser(roles = {"CUSTOMER"})
    public void deleteCategory_Forbidden() throws Exception {
        UUID categoryId = UUID.randomUUID();

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/category/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(document("delete-category-forbidden",
                        pathParameters(
                                parameterWithName("categoryId").description("삭제할 카테고리의 UUID")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과 데이터 (삭제된 카테고리 정보, null일 수 있음)"),
                                fieldWithPath("data.id").type(JsonFieldType.STRING).optional().description("카테고리 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).optional().description("카테고리 이름"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING).optional().description("생성 일시"),
                                fieldWithPath("data.createdBy").type(JsonFieldType.STRING).optional().description("생성자"),
                                fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).optional().description("수정 일시"),
                                fieldWithPath("data.updatedBy").type(JsonFieldType.STRING).optional().description("수정자"),
                                fieldWithPath("data.deletedAt").type(JsonFieldType.STRING).optional().description("삭제 일시"),
                                fieldWithPath("data.deletedBy").type(JsonFieldType.STRING).optional().description("삭제자"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드")
                        )
                ));
    }
}
