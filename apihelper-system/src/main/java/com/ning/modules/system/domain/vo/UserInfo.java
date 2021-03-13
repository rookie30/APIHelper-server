package com.ning.modules.system.domain.vo;

import lombok.Data;

@Data
public class UserInfo {

    private String username;

    private String nickname;

    private String email;

    private String phone;

    private Integer gender;
}
