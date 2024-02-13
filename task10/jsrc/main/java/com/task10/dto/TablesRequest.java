package com.task10.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class TablesRequest
{
    private int id;
    private int number;
    private int places;
    private boolean isVip;
    private int minOrder;
}
