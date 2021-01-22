package com.ning.modules.security.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class UserDto {

    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String email;

    private Integer gender;

    private Integer role;

    private Date createTime;

    private Date updateTime;

    private String updateBy;

    private String phone;

    private Boolean enabled;

    private String avatarPath;
}
