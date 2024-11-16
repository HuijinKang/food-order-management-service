package org.sparta.foodordermanagementservice.service.Impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sparta.foodordermanagementservice.dto.request.StoreRegistrationRequestDTO;
import org.sparta.foodordermanagementservice.dto.request.StoreUpdateRequestDTO;
import org.sparta.foodordermanagementservice.dto.response.StoreUpdateResponseDTO;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.repository.CategoryRepository;
import org.sparta.foodordermanagementservice.repository.StoreRepository;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class StoreServiceImplTest {

    @InjectMocks
    private StoreServiceImpl storeService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private UUID storeId;
    private Store store;
    private User user;
    private Category category;

    @BeforeEach
    void setUp() {
        storeId = UUID.randomUUID();
        user = new User();
        user.setUsername("testUser");

        category = new Category();
        category.setId(UUID.randomUUID());
        category.setName("식당");

        store = Store.builder()
                .id(storeId)
                .name("테스트 가게")
                .latitude(37.5665)
                .longitude(126.9780)
                .region("서울")
                .categories(new HashSet<>(Collections.singletonList(category)))
                .user(user)
                .createdAt(LocalDateTime.now())
                .createdBy(user.getUsername())
                .build();
    }

    @Test
    @DisplayName("가게 ID로 가게 조회 - 성공")
    void testGetStoreById_Success() {
        // given
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        // when
        Store foundStore = storeService.getStoreById(storeId);

        // then
        assertNotNull(foundStore);
        assertEquals(store.getName(), foundStore.getName());
        verify(storeRepository, times(1)).findById(storeId);
    }

    @Test
    @DisplayName("가게 ID로 가게 조회 - 실패 (가게 없음)")
    void testGetStoreById_NotFound() {
        // given
        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            storeService.getStoreById(storeId);
        });
        assertEquals("Store not found with id: " + storeId, exception.getMessage());
        verify(storeRepository, times(1)).findById(storeId);
    }

    @Test
    @DisplayName("가게 등록 - 성공")
    void testRegisterStore_Success() {
        // given
        StoreRegistrationRequestDTO requestDTO = StoreRegistrationRequestDTO.builder()
                .name("새로운 가게")
                .latitude(37.5665)
                .longitude(126.9780)
                .region("서울")
                .category(Collections.singletonList(category.getId()))
                .build();

        when(storeRepository.existsByName(requestDTO.getName())).thenReturn(false);
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Store registeredStore = storeService.registerStore(requestDTO, user);

        // then
        assertNotNull(registeredStore);
        assertEquals(requestDTO.getName(), registeredStore.getName());
        verify(storeRepository, times(1)).existsByName(requestDTO.getName());
        verify(categoryRepository, times(1)).findById(category.getId());
        verify(storeRepository, times(1)).save(any(Store.class));
    }

    @Test
    @DisplayName("가게 등록 - 실패 (이름 중복)")
    void testRegisterStore_DuplicateName() {
        // given
        StoreRegistrationRequestDTO requestDTO = StoreRegistrationRequestDTO.builder()
                .name("중복 가게")
                .build();

        when(storeRepository.existsByName(requestDTO.getName())).thenReturn(true);

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            storeService.registerStore(requestDTO, user);
        });
        assertEquals("Store with the same name already exists: " + requestDTO.getName(), exception.getMessage());
        verify(storeRepository, times(1)).existsByName(requestDTO.getName());
    }

    @Test
    @DisplayName("가게 정보 수정 - 성공")
    void testUpdateStore_Success() {
        // given
        StoreUpdateRequestDTO updateRequestDTO = StoreUpdateRequestDTO.builder()
                .name("업데이트된 가게")
                .latitude(37.5665)
                .longitude(126.9780)
                .region("서울")
                .category(Collections.singletonList(category.getId()))
                .build();

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(storeRepository.existsByName(updateRequestDTO.getName())).thenReturn(false);
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        StoreUpdateResponseDTO responseDTO = storeService.updateStore(storeId, updateRequestDTO, user);

        // then
        assertNotNull(responseDTO);
        assertEquals(updateRequestDTO.getName(), responseDTO.getName());
        verify(storeRepository, times(1)).findById(storeId);
        verify(storeRepository, times(1)).existsByName(updateRequestDTO.getName());
        verify(categoryRepository, times(1)).findById(category.getId());
        verify(storeRepository, times(1)).save(any(Store.class));
    }

    @Test
    @DisplayName("가게 삭제 - 성공")
    void testDeleteStore_Success() {
        // given
        when(storeRepository.existsById(storeId)).thenReturn(true);
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Store deletedStore = storeService.deleteStore(storeId, user);

        // then
        assertNotNull(deletedStore.getDeletedAt());
        assertEquals(user.getUsername(), deletedStore.getDeletedBy());
        verify(storeRepository, times(1)).existsById(storeId);
        verify(storeRepository, times(1)).findById(storeId);
        verify(storeRepository, times(1)).save(any(Store.class));
    }

    @Test
    @DisplayName("가게 검색 - 성공 (카테고리명으로 검색)")
    void testGetSearchStoreList_ByCategoryName() {
        // given
        String keyword = "식당"; // 카테고리명
        double latitude = 37.5665;   // 서울시청 위도
        double longitude = 126.9780; // 서울시청 경도
        int pageSize = 10;
        int pageNumber = 0;
        String sortBy = "createdAt";
        boolean isAsc = true;

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize,
                Sort.by(isAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));

        // 카테고리에 해당하는 가게 생성
        Store store1 = Store.builder()
                .id(UUID.randomUUID())
                .name("가게1")
                .latitude(37.5665)
                .longitude(126.9780)
                .categories(new HashSet<>(Collections.singletonList(category))) // "식당" 카테고리
                .build();

        Store store2 = Store.builder()
                .id(UUID.randomUUID())
                .name("가게2")
                .latitude(37.5700)
                .longitude(126.9820)
                .categories(new HashSet<>(Collections.singletonList(category))) // "식당" 카테고리
                .build();

        // 다른 카테고리에 해당하는 가게 생성
        Category otherCategory = new Category();
        otherCategory.setId(UUID.randomUUID());
        otherCategory.setName("카페");

        Store store3 = Store.builder()
                .id(UUID.randomUUID())
                .name("가게3")
                .latitude(37.5600)
                .longitude(126.9750)
                .categories(new HashSet<>(List.of(otherCategory))) // "카페" 카테고리
                .build();

        // 키워드에 매칭되는 가게들만 포함하도록 수정
        List<Store> matchingStores = Arrays.asList(store1, store2);
        Page<Store> page = new PageImpl<>(matchingStores, pageRequest, matchingStores.size());

        when(storeRepository.searchStores(keyword, pageRequest)).thenReturn(page);

        // when
        Page<Store> resultPage = storeService.getSearchStoreList(keyword, latitude, longitude, pageSize, pageNumber, sortBy, isAsc);

        // then
        assertNotNull(resultPage);
        // "식당" 카테고리에 해당하는 가게는 store1, store2
        assertEquals(2, resultPage.getTotalElements());
        List<Store> filteredStores = resultPage.getContent();
        assertTrue(filteredStores.contains(store1));
        assertTrue(filteredStores.contains(store2));
        assertFalse(filteredStores.contains(store3));

        verify(storeRepository, times(1)).searchStores(keyword, pageRequest);
    }

    @Test
    @DisplayName("가게 검색 - 성공 (가게명으로 검색)")
    void testGetSearchStoreList_ByStoreName() {
        // given
        String keyword = "특별한 가게";
        double latitude = 37.5665;   // 서울시청 위도
        double longitude = 126.9780; // 서울시청 경도
        int pageSize = 10;
        int pageNumber = 0;
        String sortBy = "createdAt";
        boolean isAsc = true;

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(isAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));

        // 검색하고자 하는 가게 생성
        Store store1 = Store.builder()
                .id(UUID.randomUUID())
                .name("특별한 가게")
                .latitude(37.5665)
                .longitude(126.9780)
                .build();

        // 다른 이름의 가게 생성
        Store store2 = Store.builder()
                .id(UUID.randomUUID())
                .name("일반 가게")
                .latitude(37.5700)
                .longitude(126.9820)
                .build();

        // 키워드에 매칭되는 가게만 포함하도록 수정
        List<Store> matchingStores = Collections.singletonList(store1);
        Page<Store> page = new PageImpl<>(matchingStores, pageRequest, matchingStores.size());

        when(storeRepository.searchStores(keyword, pageRequest)).thenReturn(page);

        // when
        Page<Store> resultPage = storeService.getSearchStoreList(keyword, latitude, longitude, pageSize, pageNumber, sortBy, isAsc);

        // then
        assertNotNull(resultPage);
        // 가게명에 "특별한 가게"가 포함된 가게는 store1
        assertEquals(1, resultPage.getTotalElements());
        List<Store> filteredStores = resultPage.getContent();
        assertTrue(filteredStores.contains(store1));
        assertFalse(filteredStores.contains(store2));

        verify(storeRepository, times(1)).searchStores(keyword, pageRequest);
    }

    @Test
    @DisplayName("가게 검색 - 실패 (유효하지 않은 키워드)")
    void testGetSearchStoreList_InvalidKeyword() {
        // given
        String keyword = "   ";

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            storeService.getSearchStoreList(keyword, 0, 0, 10, 0, "createdAt", true);
        });
        assertEquals("Keyword cannot be null or empty.", exception.getMessage());
    }
}
