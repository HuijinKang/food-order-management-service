package org.sparta.foodordermanagementservice.dto.request;


import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@ToString
@Slf4j


@Value
@Builder
public class ReqOrderedMenu {

    UUID menuId;
    int amount;

}
