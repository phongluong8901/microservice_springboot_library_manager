package com.ltfullstack.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequestDTIO {
    String username;
    String password;
}
