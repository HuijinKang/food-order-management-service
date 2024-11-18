package org.sparta.foodordermanagementservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sparta.foodordermanagementservice.dto.request.PaginateOrdersReqCondition;
import org.sparta.foodordermanagementservice.dto.request.SortedBy;
import org.sparta.foodordermanagementservice.dto.response.ResPagedOrderObj;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderStatus;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderType;
import org.sparta.foodordermanagementservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class})
class OrderControllerTest {

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private static final String testUsername = "testUser";

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {

        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(provider))
                .build();
    }


    @Test
    @WithMockUser(username = testUsername, roles = {"OWNER"})
    @DisplayName("주문 목록 조회 성공")
    void paginateOrders() throws Exception {

        //given for controller only
        ResPagedOrderObj testResObj
                = ResPagedOrderObj.builder()
                .storeId(String.valueOf(UUID.randomUUID()))
                .status(OrderStatus.WAIT)
                .type(OrderType.DELIVERY)
                .address("testAddress")
                .totalPrice(10000)
                .build();

        int pageNumber = 0, pageSize = 1, totalElements = 1;

        when(orderService.paginateOrders(any(), any()))
                .thenReturn(new PageImpl<>(
                        List.of(testResObj)
                        , PageRequest.of(pageNumber, pageSize)
                        , totalElements)
                );

        PaginateOrdersReqCondition condition = PaginateOrdersReqCondition.USER_NAME;
        mockMvc.perform(get("/api/orders")
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .param("condition", condition.getQueryString())
                        .param("key", testUsername)
                        .param("pageSize", String.valueOf(pageSize))
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("sortedBy", SortedBy.CREATED_AT.getQueryString())
                        .param("isAsc", "true")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalPages").value(totalElements / pageSize))
                .andExpect(jsonPath("$.data.totalElements").value(totalElements))
                .andExpect(jsonPath("$.data.size").value(pageSize))
                .andExpect(jsonPath("$.data.number").value(pageNumber))
                .andExpect(jsonPath("$.data.content[0].storeId").value(testResObj.getStoreId()))
                .andExpect(jsonPath("$.data.content[0].status").value(testResObj.getStatus().getQueryString()))
                .andExpect(jsonPath("$.data.content[0].type").value(testResObj.getType().getQueryString()))
                .andExpect(jsonPath("$.data.content[0].address").value(testResObj.getAddress()))
                .andExpect(jsonPath("$.data.content[0].totalPrice").value(testResObj.getTotalPrice()))

                .andDo(print())
                .andDo(document("order-paginateOrders",
                        queryParameters(
                                parameterWithName("condition").description("조회 조건"),
                                parameterWithName("key").description("조회 조건에 해당하는 값"),
                                parameterWithName("pageSize").description("페이지 크기"),
                                parameterWithName("pageNumber").description("페이지 번호"),
                                parameterWithName("sortedBy").description("정렬 기준"),
                                parameterWithName("isAsc").description("오름차순 여부")
                        )
                ));


    }



}