package org.sparta.foodordermanagementservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.request.SearchRequestDto;
import org.sparta.foodordermanagementservice.dto.request.StoreRegistrationRequestDTO;
import org.sparta.foodordermanagementservice.dto.request.StoreUpdateRequestDTO;
import org.sparta.foodordermanagementservice.dto.response.StoreUpdateResponseDTO;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.entity.UserStatus;
import org.sparta.foodordermanagementservice.security.UserDetailsImpl;
import org.sparta.foodordermanagementservice.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class})
public class StoreControllerTest {
    @MockBean
    private StoreService storeService;

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
    @DisplayName("가게 단건 조회 성공")
    public void getStoreById() throws Exception {
        UUID storeId = UUID.randomUUID();
        Store store = new Store();
        store.setId(storeId);
        store.setName("Test Store");
        store.setRegion(null);
        store.setLatitude(0.0);
        store.setLongitude(0.0);
        store.setCategories(null);
        store.setTotalRating(0);
        store.setReviewCount(0);
        store.setDeletedAt(null);
        store.setDeletedBy(null);

        given(storeService.getStoreById(storeId)).willReturn(store);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/stores/{storeId}", storeId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-store-by-id",
                        pathParameters(
                                parameterWithName("storeId").description("조회할 가게의 UUID")
                        ),
                        responseFields(
                                fieldWithPath("data.id").description("가게 ID"),
                                fieldWithPath("data.name").description("가게 이름"),
                                fieldWithPath("data.createdAt").description("생성 일시"),
                                fieldWithPath("data.createdBy").description("생성자"),
                                fieldWithPath("data.updatedAt").description("수정 일시"),
                                fieldWithPath("data.updatedBy").description("수정자"),
                                fieldWithPath("data.region").description("지역 정보"),
                                fieldWithPath("data.latitude").description("위도"),
                                fieldWithPath("data.longitude").description("경도"),
                                fieldWithPath("data.categories").description("카테고리 목록"),
                                fieldWithPath("data.totalRating").description("총 평점"),
                                fieldWithPath("data.reviewCount").description("리뷰 수"),
                                fieldWithPath("data.deletedAt").description("삭제 일시"),
                                fieldWithPath("data.deletedBy").description("삭제자"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과데이터")
                        )
                ));
    }

    @Test
    @DisplayName("가게 단건 조회 실패 - 없는 가게")
    @WithMockUser(username = "testUser", roles = {"MANAGER", "MASTER"})
    void getStoreFailWhenStoreNotFound() throws Exception {
        UUID storeId = UUID.randomUUID();
        when(storeService.getStoreById(any(UUID.class))).thenThrow(new CustomException(ErrorCode.NOT_FOUND_RESOURCE));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/stores/{storeId}", storeId)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}"))
                .andExpect(status().is(ErrorCode.NOT_FOUND_RESOURCE.getStatus().value()))
                .andDo(print())
                .andDo(document("get-store-fail-store-not-found",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과데이터")
                        )));
    }


    @Test
    @DisplayName("가게 조회 성공")
    public void searchStore() throws Exception {
        double latitude = 37.5665;
        double longitude = 126.978;
        String keyword = "JP";
        int pageNumber = 0;
        int pageSize = 10;
        boolean asc = true;
        String sortedBy = "createdAt";

        Store store = new Store();
        store.setId(UUID.randomUUID());
        store.setName("JP");
        store.setRegion(null);
        store.setLatitude(0.0);
        store.setLongitude(0.0);
        store.setCategories(null);
        store.setTotalRating(0);
        store.setReviewCount(0);
        store.setDeletedAt(null);
        store.setDeletedBy(null);

        List<Store> stores = List.of(store);
        Page<Store> storePage = new PageImpl<>(stores, PageRequest.of(pageNumber, pageSize), stores.size());

        given(storeService.getSearchStoreList(eq(latitude), eq(longitude), any(SearchRequestDto.class))).willReturn(storePage);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/stores/search")
                        .param("latitude", String.valueOf(latitude))
                        .param("longitude", String.valueOf(longitude))
                        .param("keyword", keyword)
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("asc", String.valueOf(asc))
                        .param("sortedBy", sortedBy)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("search-store",
                        queryParameters(
                                parameterWithName("latitude").description("위도 좌표"),
                                parameterWithName("longitude").description("경도 좌표"),
                                parameterWithName("keyword").description("검색 키워드"),
                                parameterWithName("pageNumber").description("페이지 번호"),
                                parameterWithName("pageSize").description("페이지 크기"),
                                parameterWithName("asc").description("오름차순 정렬 여부 (true 또는 false)"),
                                parameterWithName("sortedBy").description("정렬 기준 필드")
                        ),
                        responseFields(
                                fieldWithPath("data.content[].id").description("가게 ID"),
                                fieldWithPath("data.content[].name").description("가게 이름"),
                                fieldWithPath("data.content[].createdAt").description("생성 일시"),
                                fieldWithPath("data.content[].createdBy").description("생성자"),
                                fieldWithPath("data.content[].updatedAt").description("수정 일시"),
                                fieldWithPath("data.content[].updatedBy").description("수정자"),
                                fieldWithPath("data.content[].region").description("지역 정보"),
                                fieldWithPath("data.content[].latitude").description("위도"),
                                fieldWithPath("data.content[].longitude").description("경도"),
                                fieldWithPath("data.content[].categories").description("카테고리 목록"),
                                fieldWithPath("data.content[].totalRating").description("총 평점"),
                                fieldWithPath("data.content[].reviewCount").description("리뷰 수"),
                                fieldWithPath("data.content[].deletedAt").description("삭제 일시"),
                                fieldWithPath("data.content[].deletedBy").description("삭제자"),
                                fieldWithPath("data.pageable.sort.sorted").description("페이징 정렬 여부"),
                                fieldWithPath("data.pageable.sort.unsorted").description("페이징 비정렬 여부"),
                                fieldWithPath("data.pageable.sort.empty").description("페이징 정렬 정보 비어있는지 여부"),
                                fieldWithPath("data.pageable.pageNumber").description("현재 페이지 번호"),
                                fieldWithPath("data.pageable.pageSize").description("페이지 크기"),
                                fieldWithPath("data.pageable.offset").description("오프셋"),
                                fieldWithPath("data.pageable.paged").description("페이징 여부"),
                                fieldWithPath("data.pageable.unpaged").description("비페이징 여부"),
                                fieldWithPath("data.totalPages").description("총 페이지 수"),
                                fieldWithPath("data.totalElements").description("총 요소 수"),
                                fieldWithPath("data.last").description("마지막 페이지 여부"),
                                fieldWithPath("data.size").description("페이지 크기"),
                                fieldWithPath("data.number").description("현재 페이지 번호"),
                                fieldWithPath("data.sort.sorted").description("정렬 여부"),
                                fieldWithPath("data.sort.unsorted").description("비정렬 여부"),
                                fieldWithPath("data.sort.empty").description("정렬 정보 비어있는지 여부"),
                                fieldWithPath("data.numberOfElements").description("현재 페이지의 요소 수"),
                                fieldWithPath("data.first").description("첫 번째 페이지 여부"),
                                fieldWithPath("data.empty").description("현재 페이지가 비어있는지 여부"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과데이터")
                        )
                ));
    }



    @Test
    @DisplayName("가게 등록 성공")
    @WithMockUser(username = "testUser", roles = {"MASTER"})
    public void registerStore() throws Exception {
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

        StoreRegistrationRequestDTO requestDTO = StoreRegistrationRequestDTO.builder()
                .region("Seoul")
                .latitude(37.5665)
                .longitude(126.9780)
                .name("Test Store")
                .category(List.of(UUID.randomUUID()))
                .build();

        Store store = Store.builder()
                .id(UUID.randomUUID())
                .region(requestDTO.getRegion())
                .latitude(requestDTO.getLatitude())
                .longitude(requestDTO.getLongitude())
                .name(requestDTO.getName())
                .categories(Set.of())
                .build();

        given(storeService.registerStore(any(StoreRegistrationRequestDTO.class), eq(testUser))).willReturn(store);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("register-store",
                        requestFields(
                                fieldWithPath("region").description("지역 정보"),
                                fieldWithPath("latitude").description("위도"),
                                fieldWithPath("longitude").description("경도"),
                                fieldWithPath("name").description("가게 이름"),
                                fieldWithPath("category").description("카테고리 UUID 목록")
                        ),
                        responseFields(
                                fieldWithPath("data.id").description("가게 ID"),
                                fieldWithPath("data.region").description("지역 정보"),
                                fieldWithPath("data.latitude").description("위도"),
                                fieldWithPath("data.longitude").description("경도"),
                                fieldWithPath("data.name").description("가게 이름"),
                                fieldWithPath("data.categories").description("카테고리 목록"),
                                fieldWithPath("data.totalRating").description("총 평점"),
                                fieldWithPath("data.reviewCount").description("리뷰 수"),
                                fieldWithPath("data.createdAt").description("생성 일시"),
                                fieldWithPath("data.createdBy").description("생성자"),
                                fieldWithPath("data.updatedAt").description("수정 일시"),
                                fieldWithPath("data.updatedBy").description("수정자"),
                                fieldWithPath("data.deletedAt").description("삭제 일시"),
                                fieldWithPath("data.deletedBy").description("삭제자"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과데이터")
                        )
                ));
    }

    @Test
    @DisplayName("가게 등록 실패 - 접근 권한 없음")
    @WithMockUser(username = "testUser", roles = {"CUSTOMER", "OWNER"})
    void registerStoreFailWhenForbidden() throws Exception {
        StoreRegistrationRequestDTO requestDTO = StoreRegistrationRequestDTO.builder()
                .region("Seoul")
                .latitude(37.5665)
                .longitude(126.9780)
                .name("Test Store")
                .category(List.of(UUID.randomUUID()))
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}"))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andDo(document("register-store-fail-forbidden",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과데이터")
                        )));
    }

    @Test
    @DisplayName("가게 수정 성공")
    @WithMockUser(username = "testUser", roles = {"OWNER", "MANAGER", "MASTER"})
    void updateStoreSuccess() throws Exception {
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

        UUID storeId = UUID.randomUUID();
        StoreUpdateRequestDTO requestDTO = StoreUpdateRequestDTO.builder()
                .name("Updated Store Name")
                .region("Updated Region")
                .latitude(37.5678)
                .longitude(126.9784)
                .category(List.of(UUID.randomUUID()))
                .build();

        StoreUpdateResponseDTO responseDTO = StoreUpdateResponseDTO.builder()
                .id(storeId)
                .name(requestDTO.getName())
                .region(requestDTO.getRegion())
                .latitude(requestDTO.getLatitude())
                .longitude(requestDTO.getLongitude())
                .categories(null)
                .updatedAt(null)
                .updatedBy(null)
                .build();

        when(storeService.updateStore(any(UUID.class), any(StoreUpdateRequestDTO.class), eq(testUser)))
                .thenReturn(responseDTO);

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/stores/{storeId}", storeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("update-store-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("수정할 가게 이름"),
                                fieldWithPath("region").description("수정할 지역 정보"),
                                fieldWithPath("latitude").description("수정할 위도"),
                                fieldWithPath("longitude").description("수정할 경도"),
                                fieldWithPath("category").description("수정할 카테고리 목록")
                        ),
                        responseFields(
                                fieldWithPath("data.id").type(JsonFieldType.STRING).description("가게 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("수정된 가게 이름"),
                                fieldWithPath("data.region").type(JsonFieldType.STRING).description("수정된 지역 정보"),
                                fieldWithPath("data.latitude").type(JsonFieldType.NUMBER).description("수정된 위도"),
                                fieldWithPath("data.longitude").type(JsonFieldType.NUMBER).description("수정된 경도"),
                                fieldWithPath("data.categories").type(JsonFieldType.ARRAY).optional().description("수정된 카테고리 목록"),
                                fieldWithPath("data.updatedAt").type(JsonFieldType.NULL).optional().description("수정된 시간"),
                                fieldWithPath("data.updatedBy").type(JsonFieldType.NULL).optional().description("수정한 사람"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드")
                        )));
    }

    @Test
    @DisplayName("가게 수정 실패 - 없는 가게")
    @WithMockUser(username = "testUser", roles = {"OWNER", "MANAGER", "MASTER"})
    void updateStoreFailWhenStoreNotFound() throws Exception {
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

        UUID storeId = UUID.randomUUID();
        StoreUpdateRequestDTO requestDTO = StoreUpdateRequestDTO.builder()
                .name("Updated Store Name")
                .region("Updated Region")
                .latitude(37.5678)
                .longitude(126.9784)
                .category(List.of(UUID.randomUUID()))
                .build();

        when(storeService.updateStore(any(UUID.class), any(StoreUpdateRequestDTO.class), eq(testUser)))
                .thenThrow(new CustomException(ErrorCode.NOT_FOUND_RESOURCE));

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/stores/{storeId}", storeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}"))
                .andExpect(status().is(ErrorCode.NOT_FOUND_RESOURCE.getStatus().value()))
                .andDo(print())
                .andDo(document("update-store-fail-store-not-found",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("수정할 가게 이름"),
                                fieldWithPath("region").description("수정할 지역 정보"),
                                fieldWithPath("latitude").description("수정할 위도"),
                                fieldWithPath("longitude").description("수정할 경도"),
                                fieldWithPath("category").description("수정할 카테고리 목록")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과데이터")
                        )));
    }

    @Test
    @DisplayName("가게 삭제 성공")
    @WithMockUser(username = "testUser", roles = {"MASTER"})
    void deleteStoreSuccess() throws Exception {
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

        UUID storeId = UUID.randomUUID();
        Store store = Store.builder()
                .id(storeId)
                .name("Test Store")
                .region("Test Region")
                .latitude(37.5678)
                .longitude(126.9784)
                .categories(null)
                .totalRating(0)
                .reviewCount(0)
                .deletedAt(null)
                .deletedBy(null)
                .build();

        when(storeService.deleteStore(any(UUID.class), eq(testUser))).thenReturn(store);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/stores/{storeId}", storeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("delete-store-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("data.id").type(JsonFieldType.STRING).description("가게 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("삭제된 가게 이름"),
                                fieldWithPath("data.region").type(JsonFieldType.STRING).description("삭제된 지역 정보"),
                                fieldWithPath("data.latitude").type(JsonFieldType.NUMBER).description("삭제된 위도"),
                                fieldWithPath("data.longitude").type(JsonFieldType.NUMBER).description("삭제된 경도"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.NULL).optional().description("생성 일시"),
                                fieldWithPath("data.createdBy").type(JsonFieldType.NULL).optional().description("생성자"),
                                fieldWithPath("data.updatedAt").type(JsonFieldType.NULL).optional().description("수정 일시"),
                                fieldWithPath("data.updatedBy").type(JsonFieldType.NULL).optional().description("수정자"),
                                fieldWithPath("data.categories").type(JsonFieldType.ARRAY).optional().description("카테고리 목록"),
                                fieldWithPath("data.totalRating").type(JsonFieldType.NUMBER).optional().description("총 평점"),
                                fieldWithPath("data.reviewCount").type(JsonFieldType.NUMBER).optional().description("리뷰 수"),
                                fieldWithPath("data.deletedAt").type(JsonFieldType.NULL).optional().description("삭제 일시"),
                                fieldWithPath("data.deletedBy").type(JsonFieldType.NULL).optional().description("삭제자"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드")
                        )));
    }

    @Test
    @DisplayName("가게 삭제 실패 - 없는 가게")
    @WithMockUser(username = "testUser", roles = {"MASTER"})
    void deleteStoreFailWhenStoreNotFound() throws Exception {
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

        UUID storeId = UUID.randomUUID();

        when(storeService.deleteStore(any(UUID.class), eq(testUser))).thenThrow(new CustomException(ErrorCode.NOT_FOUND_RESOURCE));

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/stores/{storeId}", storeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}"))
                .andExpect(status().is(ErrorCode.NOT_FOUND_RESOURCE.getStatus().value()))
                .andDo(print())
                .andDo(document("delete-store-fail-store-not-found",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("결과데이터")
                        )));
    }
}