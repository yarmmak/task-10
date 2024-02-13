package com.task10.dto;


import com.task10.model.TablesModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter @Setter
public class TablesResponse
{
    private List<TablesModel> tables;
}

