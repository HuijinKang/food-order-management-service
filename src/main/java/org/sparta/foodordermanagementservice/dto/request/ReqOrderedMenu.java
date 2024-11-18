package org.sparta.foodordermanagementservice.dto.request;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@ToString
@Slf4j


@Value
public class ReqOrderedMenu {

    UUID menuId;
    int amount;
    int menuPrice;

    @JsonCreator
    @Builder
    public ReqOrderedMenu(UUID menuId, int amount, int menuPrice) {
        this.menuId = menuId;
        this.amount = amount;
        this.menuPrice = menuPrice;
    }

}
