package com.ning.modules.system.service;

import com.ning.modules.security.dto.UserDto;
import com.ning.modules.system.pojo.User;
import org.springframework.stereotype.Service;

public interface UserService {

    /**
     * 创建用户
     * @param resource
     */
    void create(User resource);

    /**
     * 编辑用户
     * @param resource
     */
    void update(User resource);

    /**
     * 根据账号查询用户
     * @param username
     * @return
     */
    UserDto findByName(String username);

    /**
     * 修改邮箱
     * @param username 账号
     * @param email 邮箱
     */
    void updateEmail(String username, String email);

    void updatePassword(String username, String password);
}
