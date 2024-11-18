package org.sparta.foodordermanagementservice.total;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sparta.foodordermanagementservice.repository.OrderDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@Slf4j


@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderIntegratedTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderDAO orderDAO;

//    @Autowired
//    private OrderRepository orderRepository;

//    @BeforeEach
//    void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {
//        mockMvc = MockMvcBuilders.webAppContextSetup(context)
//                .addFilter(new CharacterEncodingFilter("UTF-8", true))
//                .apply(documentationConfiguration(provider))
//                .build();
//    }

    @Test
    @WithMockUser(username = "testUser", roles = {"MASTER", "USER", "OWNER", "ADMIN"})
    public void testPaginateOrders() throws Exception {

////        JPAQuery queryMock = Mockito.mock(JPAQuery.class);
//
//        // given
//        int pageSize = 10, pageNumber = 0;
//
//
//        String condition = "username";
//        User user = User.builder().id(1L)
//                .username("nara")
//                .password("password").build();
//        Store store = Store.builder()
//                .id(UUID.randomUUID()).build();
//
//        SortedBy sortedBy = SortedBy.valueOf("CREATED_AT");
//        boolean isAsc = true;
//
//        PaginateOrdersDTO paginateOrdersDTO = PaginateOrdersDTO.builder()
//                .storeId(store.getId())
//                .username(user.getUsername())
//                .pageSize(pageSize)
//                .pageNumber(pageNumber)
//                .sortedBy(sortedBy)
//                .isAsc(isAsc)
//                .build();
//
//        List<Order> currentPageOrders
//                = List.of(
//                Order.builder().user(user).store(store)
//                        .status(OrderStatus.ACCEPT).type(OrderType.DELIVERY).address("address")
//                        .comment("comment").totalPrice(0).build()
//        );
//
//
//        when(orderDAO.countTotal(store.getId(), user.getUsername()))
//                .thenReturn(1L);
//
//        when(orderDAO.readCurrentPage(paginateOrdersDTO))
//                .thenReturn(currentPageOrders);
//        orderRepository.setOrderDao(orderDAO);
////        log.info("orderDaoReturn: {}", orderDAO.readCurrentPage(paginateOrdersDTO).get(0).toString());
//
//        ResultActions resultActions
//                = mockMvc.perform(get("/api/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .param("condition", condition)
//                        .param("key", user.getUsername())
//                        .param("pageSize", String.valueOf(pageSize))
//                        .param("pageNumber", String.valueOf(pageNumber))
//                        .param("sortedBy", sortedBy.getRequested())
//                        .param("isAsc", String.valueOf(isAsc))
//                        .principal(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()))
//                        .with(user("testUser").roles("USER")))
//
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.content").isArray())
//                .andExpect(jsonPath("$.data.content[0].storeId").value(store.getId().toString()))
//                .andExpect(jsonPath("$.data.content[0].type").value(OrderType.DELIVERY.name()))
//                .andExpect(jsonPath("$.data.content[0].address").value("address"))
//                .andExpect(jsonPath("$.data.content[0].totalPrice").value(0))
//                .andExpect(jsonPath("$.data.totalElements").value(1))
//                .andExpect(jsonPath("$.data.pageable.pageNumber").value(pageNumber))
//                .andExpect(jsonPath("$.data.pageable.pageSize").value(pageSize));

//                .andDo(document(
//                        "get-orders-success",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        queryParameters(
//                                parameterWithName("condition").description("검색 조건"),
//                                parameterWithName("key").description("검색값"),
//                                parameterWithName("pageSize").description("한 페이지의 데이터 수"),
//                                parameterWithName("pageNumber").description("현재 페이지 번호"),
//                                parameterWithName("sortedBy").description("정렬 기준"),
//                                parameterWithName("isAscending").description("오름차순 여부")
//                        ),
//                        responseFields(
//                                fieldWithPath("content[]").type(JsonFieldType.STRING).description("주문 목록"),
//                                fieldWithPath("content[].storeId").description("가게 ID"),
//                                fieldWithPath("content[].type").description("주문 종류: 배달/포장"),
//                                fieldWithPath("content[].address").description("가게 주소"),
//                                fieldWithPath("content[].totalPrice").description("결제 금액"),
//                                fieldWithPath("totalElements").description("전체 주문 수"),
//                                fieldWithPath("totalPages").description("전체 페이지 수"),
//                                fieldWithPath("size").description("한 페이지의 데이터 수"),
//                                fieldWithPath("number").description("현재 페이지 번호"),
//                                fieldWithPath("numberOfElements").description("현재 페이지에 포함된 데이터 수"),
//                                fieldWithPath("first").description("첫 번째 페이지 여부"),
//                                fieldWithPath("last").description("마지막 페이지 여부"),
//                                fieldWithPath("empty").description("빈 페이지 여부")
//                        )));

    }


}


//        //queryFactory Mock 설정
//        when(queryFactory.selectFrom(any())).thenReturn(queryMock);
//        when(queryMock.where((Predicate) any(Predicate.class))).thenReturn(queryMock);
//        when(queryMock.orderBy((OrderSpecifier<?>) any(OrderSpecifier.class))).thenReturn(queryMock);
//        when(queryMock.offset(any())).thenReturn(queryMock);
//        when(queryMock.limit(any())).thenReturn(queryMock);
//
//        when(queryMock.fetch()).thenReturn(daoResult);
//        when(queryMock.fetchCount()).thenReturn((long) (pageSize * pageNumber));


//        when(orderRepository.countTotalOrders(storeId, username))
//                .thenReturn((long) (pageSize * pageNumber));
//        when(orderRepository.readCurrentPageOrders(any())).thenReturn(dbResult);

//    @Test
//    @WithMockUser(username = "adminUser", roles = {"MASTER"})
//    public void testDeleteOrder() throws Exception {
//        // given
//        UUID orderId = UUID.randomUUID();
//
//        // when
//        ResultActions resultActions = mockMvc.perform(delete("/api/orders/{id}", orderId)
//                .with(user("adminUser").roles("MASTER"))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        // then
//        resultActions.andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").isEmpty()); // Void 응답 확인
//    }
