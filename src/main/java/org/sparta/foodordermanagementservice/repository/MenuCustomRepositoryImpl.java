package org.sparta.foodordermanagementservice.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.entity.Menu;
import org.sparta.foodordermanagementservice.entity.MenuStatus;
import org.sparta.foodordermanagementservice.entity.QMenu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MenuCustomRepositoryImpl implements MenuCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Menu> searchMenus(String condition, String keyword, Pageable pageable) {
        QMenu menu = QMenu.menu;

        BooleanExpression conditionExpression = buildConditionExpression(condition, keyword)
                .and(menu.status.ne(MenuStatus.DISCONTINUED));

        List<Menu> result = queryFactory.selectFrom(menu)
                .where(conditionExpression)
                .orderBy(getOrderSpecifiers(menu, pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = getTotalCount(menu, conditionExpression);

        return new PageImpl<>(result, pageable, totalCount);
    }

    // condition 과 keyword를 이용한 검색 기능
    private BooleanExpression buildConditionExpression(String condition, String keyword) {
        if ("name".equals(condition)) { // 프론트에서 셀렉트바를 이용해 name, description 으로 값이 넘어온다고 가정
            return QMenu.menu.name.containsIgnoreCase(keyword);
        } else if ("description".equals(condition)) {
            return QMenu.menu.description.containsIgnoreCase(keyword);
        }
        return QMenu.menu.name.containsIgnoreCase(keyword);
    }

    // 정렬 처리 (가격순, 시간순)
    private com.querydsl.core.types.OrderSpecifier<?>[] getOrderSpecifiers(QMenu menu, Pageable pageable) {
        return pageable.getSort().stream()
                .map(order -> getOrderSpecifier(menu, order.getProperty(), order.isAscending()))
                .toArray(com.querydsl.core.types.OrderSpecifier[]::new);
    }

    private com.querydsl.core.types.OrderSpecifier<?> getOrderSpecifier(QMenu menu, String property, boolean ascending) {
        switch (property.toLowerCase()) {
            case "price":
                return ascending ? menu.price.asc() : menu.price.desc();
            case "createdat":
                return ascending ? menu.createdAt.asc() : menu.createdAt.desc();
            default:
                return menu.createdAt.desc();
        }
    }

    // 총 갯수 검색
    private long getTotalCount(QMenu menu, BooleanExpression conditionExpression) {
        return queryFactory.selectFrom(menu)
                .where(conditionExpression)
                .fetch().size();
    }
}
