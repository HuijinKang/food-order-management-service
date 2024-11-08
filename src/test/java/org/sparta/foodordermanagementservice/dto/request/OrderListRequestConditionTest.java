package org.sparta.foodordermanagementservice.dto.request;

import org.junit.jupiter.api.Test;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
@SuppressWarnings("unused")

class OrderListRequestConditionTest {

    @Test
    void test1() {
        OrderListRequestCondition.Converter converter = new OrderListRequestCondition.Converter();

        OrderListRequestCondition condition
                = converter.convert("storeId");
        assertThat(condition).isEqualTo(OrderListRequestCondition.STORE_ID);

        condition
                = converter.convert("userName");
        assertThat(condition).isEqualTo(OrderListRequestCondition.USER_NAME);

        try {
            condition = converter.convert("no words");
        } catch (CustomException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.BAD_REQUEST);
        }


    }


}
