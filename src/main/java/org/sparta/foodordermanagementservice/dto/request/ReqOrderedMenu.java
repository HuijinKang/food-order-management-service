package org.sparta.foodordermanagementservice.dto.request;


import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@ToString
@Slf4j


@Value
@Builder
public class ReqOrderedMenu {

    Long menuId;
    int amount;

}
