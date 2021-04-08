package com.ning.modules.system.domain.vo;

import lombok.Data;

@Data
public class UserInfo {

    private String username;

    private String nickname;

    private String email;

    private String phone;

    private Integer gender;

    public UserInfo() {
        super();
    }

    public UserInfo(String username, String nickname,
                    String email, String phone, Integer gender) {
        this.username = username;
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
    }
}
