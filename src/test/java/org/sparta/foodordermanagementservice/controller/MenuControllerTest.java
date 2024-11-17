package org.sparta.foodordermanagementservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sparta.foodordermanagementservice.dto.request.MenuRequestDto;
import org.sparta.foodordermanagementservice.dto.response.MenuResponseDto;
import org.sparta.foodordermanagementservice.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
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

    @Test
    @DisplayName("메뉴 삭제 성공")
    @WithMockUser(username = "testUser", roles = {"OWNER", "MASTER"})
    void deleteMenuSuccess() throws Exception {
        UUID menuId = UUID.randomUUID();

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/menus/{menuId}", menuId)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("delete-menu-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터").optional(),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
                        )
                ));
    }


    @Test
    @DisplayName("메뉴 단건 조회 성공")
    @WithMockUser(username = "testUser", roles = {"USER", "OWNER", "MASTER"})
    void getMenuSuccess() throws Exception {
        // Given
        UUID menuId = UUID.randomUUID();

        MenuResponseDto responseDto = MenuResponseDto.builder()
                .name("테스트 메뉴")
                .price(12000)
                .description("테스트 메뉴 설명")
                .storeId(UUID.randomUUID())
                .build();

        given(menuService.getMenu(menuId)).willReturn(responseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/menus/{menuId}", menuId)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-menu-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("메뉴 이름"),
                                fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("메뉴 가격"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("메뉴 설명"),
                                fieldWithPath("data.storeId").type(JsonFieldType.STRING).description("가게 ID"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("메뉴 목록 조회 성공")
    @WithMockUser(username = "testUser", roles = {"USER", "OWNER", "MASTER"})
    void getMenusSuccess() throws Exception {
        UUID storeId = UUID.randomUUID();

        MenuResponseDto menu1 = MenuResponseDto.builder()
                .name("메뉴1")
                .price(10000)
                .description("첫 번째 메뉴 설명")
                .storeId(storeId)
                .build();

        MenuResponseDto menu2 = MenuResponseDto.builder()
                .name("메뉴2")
                .price(15000)
                .description("두 번째 메뉴 설명")
                .storeId(storeId)
                .build();

        List<MenuResponseDto> responseDtoList = List.of(menu1, menu2);

        given(menuService.getMenus(storeId)).willReturn(responseDtoList);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/menus")
                        .queryParam("storeId", storeId.toString())
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-menus-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("storeId").description("가게 ID")
                        ),
                        responseFields(
                                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("메뉴 이름"),
                                fieldWithPath("data[].price").type(JsonFieldType.NUMBER).description("메뉴 가격"),
                                fieldWithPath("data[].description").type(JsonFieldType.STRING).description("메뉴 설명"),
                                fieldWithPath("data[].storeId").type(JsonFieldType.STRING).description("가게 ID"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("메뉴 검색 성공")
    @WithMockUser(username = "testUser", roles = {"USER", "OWNER", "MASTER"})
    void searchMenusSuccess() throws Exception {
        String condition = "name";
        String keyword = "테스트";
        int pageSize = 10;
        int pageNumber = 0;
        String sortedBy = "price";
        boolean isAsc = true;

        MenuResponseDto menu1 = MenuResponseDto.builder()
                .name("테스트 메뉴1")
                .price(10000)
                .description("테스트 메뉴1 설명")
                .storeId(UUID.randomUUID())
                .build();

        MenuResponseDto menu2 = MenuResponseDto.builder()
                .name("테스트 메뉴2")
                .price(15000)
                .description("테스트 메뉴2 설명")
                .storeId(UUID.randomUUID())
                .build();

        Page<MenuResponseDto> responsePage = new PageImpl<>(List.of(menu1, menu2));

        given(menuService.searchMenus(condition, keyword, pageSize, pageNumber, sortedBy, isAsc))
                .willReturn(responsePage);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/menus/search")
                        .queryParam("condition", condition)
                        .queryParam("keyword", keyword)
                        .queryParam("pageSize", String.valueOf(pageSize))
                        .queryParam("pageNumber", String.valueOf(pageNumber))
                        .queryParam("sortedBy", sortedBy)
                        .queryParam("isAsc", String.valueOf(isAsc))
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("search-menus-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("condition").description("검색 조건 (예: name, description)"),
                                parameterWithName("keyword").description("검색 키워드"),
                                parameterWithName("pageSize").description("페이지 크기"),
                                parameterWithName("pageNumber").description("페이지 번호"),
                                parameterWithName("sortedBy").description("정렬 기준 (예: price, name)"),
                                parameterWithName("isAsc").description("오름차순 여부 (true/false)")
                        ),
                        responseFields(
                                fieldWithPath("data.content[].name").type(JsonFieldType.STRING).description("메뉴 이름"),
                                fieldWithPath("data.content[].price").type(JsonFieldType.NUMBER).description("메뉴 가격"),
                                fieldWithPath("data.content[].description").type(JsonFieldType.STRING).description("메뉴 설명"),
                                fieldWithPath("data.content[].storeId").type(JsonFieldType.STRING).description("가게 ID"),
                                fieldWithPath("data.pageable").type(JsonFieldType.STRING).description("페이지 정보 (Pageable)"),
                                fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되지 않았는지 여부"),
                                fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬되었는지 여부"),
                                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("총 검색 결과 개수"),
                                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지의 요소 수"),
                                fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("페이지가 비어 있는지 여부"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
                        )
                ));

    }




}
