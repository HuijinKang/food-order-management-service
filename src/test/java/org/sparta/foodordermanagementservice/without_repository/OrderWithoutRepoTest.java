//package org.sparta.foodordermanagementservice.without_repository;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.sparta.foodordermanagementservice.dto.OrderDTO;
//import org.sparta.foodordermanagementservice.dto.OrderedMenuDTO;
//import org.sparta.foodordermanagementservice.dto.PaginateOrdersDTO;
//import org.sparta.foodordermanagementservice.dto.request.PaginateOrdersReqCondition;
//import org.sparta.foodordermanagementservice.dto.request.ReqCreateOrder;
//import org.sparta.foodordermanagementservice.dto.request.ReqOrderedMenu;
//import org.sparta.foodordermanagementservice.dto.request.SortedBy;
//import org.sparta.foodordermanagementservice.entity.*;
//import org.sparta.foodordermanagementservice.entity.enumerate.OrderStatus;
//import org.sparta.foodordermanagementservice.entity.enumerate.OrderType;
//import org.sparta.foodordermanagementservice.repository.MenuRepository;
//import org.sparta.foodordermanagementservice.repository.OrderRepository;
//import org.sparta.foodordermanagementservice.repository.OrderedMenuRepository;
//import org.sparta.foodordermanagementservice.repository.PaymentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.restdocs.RestDocumentationContextProvider;
//import org.springframework.restdocs.RestDocumentationExtension;
//import org.springframework.restdocs.payload.JsonFieldType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.filter.CharacterEncodingFilter;
//
//import java.util.List;
//import java.util.UUID;
//
//import static org.hibernate.query.sqm.tree.SqmNode.log;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
//import static org.springframework.restdocs.payload.PayloadDocumentation.*;
//import static org.springframework.restdocs.request.RequestDocumentation.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
//class OrderWithoutRepoTest {
//
//
//    @MockBean
//    private OrderRepository orderRepo;
//    @MockBean
//    private OrderedMenuRepository orderedMenuRepo;
//    @MockBean
//    private PaymentRepository paymentRepo;
//    @MockBean
//    private MenuRepository menuRepo;
//
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    private static final String testUsername = "testUser";
//    private static final String testStoreName = "testStore";
//
//    @BeforeEach
//    void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {
//
//        mockMvc = MockMvcBuilders.webAppContextSetup(context)
//                .addFilter(new CharacterEncodingFilter("UTF-8", true))
//                .apply(documentationConfiguration(provider))
//                .build();
//    }
//
//
//    @Test
//    @WithMockUser(username = testUsername, roles = {"OWNER", "MASTER", "CUSTOMER"})
//    @DisplayName("주문 목록 조회 ")
//    void paginateOrders() throws Exception {
//
//        // given
//        PaginateOrdersReqCondition condition = PaginateOrdersReqCondition.USER_NAME;
//        String key = testUsername;
//        int pageSize = 10;
//        int pageNumber = 15;
//        SortedBy sortedBy = SortedBy.CREATED_AT;
//        boolean isAsc = true;
//
//        List<OrderDTO> repoResult = List.of(new OrderDTO[]{
//                OrderDTO.builder()
//                        .id(UUID.randomUUID())
//                        .username(testUsername)
//                        .storeId(UUID.randomUUID())
//                        .storeName(testStoreName)
//                        .status(OrderStatus.WAIT)
//                        .type(OrderType.DELIVERY)
//                        .address("asdafd")
//                        .comment("aslkdfslkadsf")
//                        .totalPrice(13)
//                        .build()
//        });
//
//        when(orderRepo.readCurrentPageOrders(any(PaginateOrdersDTO.class)))
//                .thenReturn(repoResult);
//        when(orderRepo.countTotalOrders(any(UUID.class), eq(testUsername)))
//                .thenReturn((long) repoResult.size());
//
//
//        OrderDTO result0 = repoResult.get(0);
//        mockMvc.perform(get("/api/orders")
//                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
//                        .param("condition", condition.getQueryString())
//                        .param("key",
//                                condition == PaginateOrdersReqCondition.USER_NAME
//                                        ? testUsername
//                                        : String.valueOf(repoResult.get(0).getStoreId()))
//                        .param("pageSize", String.valueOf(pageSize))
//                        .param("pageNumber", String.valueOf(pageNumber))
//                        .param("sortedBy", SortedBy.CREATED_AT.getQueryString())
//                        .param("isAsc", String.valueOf(isAsc))
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.size").value(pageSize))
//                .andExpect(jsonPath("$.data.number").value(pageNumber))
//                .andExpect(jsonPath("$.data.content[0].storeId").value(result0.getStoreId().toString()))
//                .andExpect(jsonPath("$.data.content[0].status").value(result0.getStatus().getQueryString()))
//                .andExpect(jsonPath("$.data.content[0].type").value(result0.getType().getQueryString()))
//                .andExpect(jsonPath("$.data.content[0].address").value(result0.getAddress()))
//                .andExpect(jsonPath("$.data.content[0].totalPrice").value(result0.getTotalPrice()))
//
//                .andDo(print())
//                .andDo(document("order-paginateOrders-controller&service",
//                        queryParameters(
//                                parameterWithName("condition").description("조회 조건"),
//                                parameterWithName("key").description("조회 조건에 해당하는 값"),
//                                parameterWithName("pageSize").description("페이지 크기"),
//                                parameterWithName("pageNumber").description("페이지 번호"),
//                                parameterWithName("sortedBy").description("정렬 기준"),
//                                parameterWithName("isAsc").description("오름차순 여부")
//                        )
//                ));
//
//
//    }
//
//
//    @Test
//    @WithMockUser(username = testUsername, roles = {"CUSTOMER"})
//    @DisplayName("주문 조회")
//    void readOrderDetail() throws Exception {
//        //given
//        //db response
//        User user = User.builder().username(testUsername).userRole(UserRole.CUSTOMER).build();
//
//        Order order = Order.builder()
//                .id(UUID.randomUUID())
//                .user(user)
//                .store(Store.builder().id(UUID.randomUUID()).name("store").build())
//                .status(OrderStatus.WAIT)
//                .type(OrderType.DELIVERY)
//                .address("address")
//                .comment("comment")
//                .totalPrice(1000)
//                .build();
//
//        Menu menu = Menu.builder()
//                .id(UUID.randomUUID())
//                .store(order.getStore())
//                .build();
//
//        List<OrderedMenu> orderedMenus = List.of(new OrderedMenu[]
//                {OrderedMenu.builder().order(order).menu(menu).amount(23).build()});
//
//        List<OrderedMenuDTO> orderedMenuDTOs = orderedMenus.stream().map(OrderedMenuDTO::from).toList();
//
//        Payment payment = Payment.builder().order(order).payedPrice(order.getTotalPrice()).build();
//
//        //controller request
//        UUID targetOrderId = order.getId();
////        UUID targetOrderId = UUID.randomUUID(); // 실패케이스: 존재하지 않는 주문 아이디
//
//
//        when(orderRepo.readOrder(order.getId()))
//                .thenReturn(OrderDTO.from(order));
//
//        when(orderedMenuRepo.readOrderedMenuList(targetOrderId))
//                .thenReturn(targetOrderId.equals(order.getId()) ? orderedMenuDTOs : List.of());
//
//        when(paymentRepo.readPayment()).thenReturn(payment);
//
//
//        mockMvc.perform(get("/api/orders/{id}", targetOrderId))
//                .andDo(print())
//
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.storeId").value(order.getStore().getId().toString()))
//                .andExpect(jsonPath("$.data.storeName").value(order.getStore().getName()))
//                .andExpect(jsonPath("$.data.orderStatus").value(order.getStatus().getQueryString()))
//                .andExpect(jsonPath("$.data.orderType").value(order.getType().getQueryString()))
//                .andExpect(jsonPath("$.data.address").value(order.getAddress()))
//                .andExpect(jsonPath("$.data.comment").value(order.getComment()))
//                .andExpect(jsonPath("$.data.payment.orderId").value(order.getId().toString()))
//                .andExpect(jsonPath("$.data.payment.payedPrice")
//                        .value(user.getUserRole() != UserRole.OWNER
//                                ? String.valueOf(order.getTotalPrice())
//                                : null))
//
//                .andDo(document("order-readOrder",
//                        pathParameters(parameterWithName("id").description("읽으려는 주문 아이디"))
//                ));
//
//
//    }
//
//    @Test
//    @WithMockUser(username = testUsername, roles = {"CUSTOMER"})
//    @DisplayName("주문 하기")
//    void createOrder() throws Exception {
//
//        //given
//        //request
//        List<ReqOrderedMenu> reqOrderedMenus = List.of(new ReqOrderedMenu[]{
//                ReqOrderedMenu.builder()
//                        .menuId(UUID.randomUUID())
//                        .amount(1)
//                        .menuPrice(2134)
//                        .build()
//        });
//        ReqCreateOrder reqCreateOrder
//                = ReqCreateOrder.builder().menuList(reqOrderedMenus)
//                .storeId(UUID.randomUUID()).type(OrderType.DELIVERY)
//                .address("address").comment("comment").orderType("delivery").totalPrice(2134).build();
//
//        //db response
//        List<Menu> menus =
//                List.of(new Menu[]{Menu.builder()
//                        .id(reqOrderedMenus.get(0).getMenuId())
//                        .price(reqOrderedMenus.get(0).getMenuPrice())
//                        .status(MenuStatus.ACTIVE)
//                        .build()});
//        //reqCreateOrder에 해당하는 메뉴 정보 정의
//        List<Menu> orderedMenus = List.of(new Menu[]{
//                Menu.builder().id(reqOrderedMenus.get(0).getMenuId())
//                        .price(reqOrderedMenus.get(0).getMenuPrice()).build()
//        });
//        UUID createdOrderId = UUID.randomUUID();
//
//
//        when(menuRepo.findAllById(any())).thenReturn(menus);
//        when(orderRepo.createOrder(any())).thenReturn(createdOrderId);
//
//
//        String requestJson = objectMapper.writeValueAsString(reqCreateOrder);
//        log.info("herehere " + requestJson);
//
//        mockMvc.perform(post("/api/orders")
//                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value(createdOrderId.toString()))
//
//                .andDo(document("order-readOrder",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("menuList").type(JsonFieldType.ARRAY).description("주문 요청 메뉴 목록"),
//                                fieldWithPath("menuList[].menuId").type(JsonFieldType.STRING).description("메뉴 ID"),
//                                fieldWithPath("menuList[].amount").type(JsonFieldType.NUMBER).description("메뉴 수량"),
//                                fieldWithPath("menuList[].menuPrice").type(JsonFieldType.NUMBER).description("메뉴 가격"),
//                                fieldWithPath("storeId").type(JsonFieldType.STRING).description("주문 요청 가게 ID"),
//                                fieldWithPath("type").type(JsonFieldType.STRING).description("주문 타입"),
//                                fieldWithPath("address").type(JsonFieldType.STRING).description("배송 주소"),
//                                fieldWithPath("comment").type(JsonFieldType.STRING).description("주문 요청 사항"),
//                                fieldWithPath("orderType").type(JsonFieldType.STRING).description("주문 유형"),
//                                fieldWithPath("totalPrice").type(JsonFieldType.NUMBER).description("총 주문 가격")
//                        ),
//                        responseFields(
//                                fieldWithPath("data").type(JsonFieldType.STRING).description("생성된 주문 아이디"),
//                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
//                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드")
//                        )));
//
//    }
//
//
//    @Test
//    @WithMockUser(username = testUsername, roles = {"CUSTOMER"})
//    @DisplayName("주문 하기")
//    void updateOrder() {
//    }
//
//    @Test
//    @WithMockUser(username = testUsername, roles = {"CUSTOMER"})
//    @DisplayName("주문 하기")
//    void deleteOrder() {
//    }
//}
