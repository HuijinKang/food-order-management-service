package org.sparta.foodordermanagementservice.dto.request;

import org.junit.jupiter.api.Test;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
@SuppressWarnings("unused")

class PaginateOrdersReqConditionTest {

    @Test
    void test1() {
        PaginateOrdersReqCondition.Converter converter = new PaginateOrdersReqCondition.Converter();

        PaginateOrdersReqCondition condition
                = converter.convert("storeId");
        assertThat(condition).isEqualTo(PaginateOrdersReqCondition.STORE_ID);

        condition
                = converter.convert("username");
        assertThat(condition).isEqualTo(PaginateOrdersReqCondition.USER_NAME);

        try {
            condition = converter.convert("no words");
        } catch (CustomException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.BAD_REQUEST);
        }


    }


}
