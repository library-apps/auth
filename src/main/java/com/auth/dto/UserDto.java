package com.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateUserDto {

        private String username;
        private String email;
        private String password;
        private String phone;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SetRoleDto {

        private String username;
        private String rolename;

    }
}
