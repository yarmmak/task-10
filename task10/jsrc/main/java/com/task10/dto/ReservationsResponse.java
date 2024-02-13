package com.task10.dto;

import com.task10.model.ReservationsModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter @Setter
public class ReservationsResponse
{
    private List<ReservationsModel> reservations;
}
