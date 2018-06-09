package com.nanoporeqc.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordUserDto {

    private String username;

    private String oldPassword;

    private String newPassword;

}
