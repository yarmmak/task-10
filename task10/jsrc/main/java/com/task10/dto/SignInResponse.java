package com.task10.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class SignInResponse {
    private String accessToken;
}
