package com.task10.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class SignUpRequestData {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
