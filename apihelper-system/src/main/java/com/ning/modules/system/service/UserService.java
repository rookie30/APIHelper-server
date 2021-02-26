package com.ning.modules.system.service;

import com.ning.modules.security.dto.UserDto;
import com.ning.modules.system.domain.Role;
import com.ning.modules.system.domain.User;

import java.util.Date;
import java.util.Set;

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
//    UserDto findByName(String username);
    User findByName(String username);

    /**
     * 修改邮箱
     * @param username 账号
     * @param email 邮箱
     */
    void updateEmail(String username, String email);

    /**
     * 删除用户
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 修改密码
     * @param username 用户名
     * @param encryptPassword 密码
     */
    void updatePass(String username, String encryptPassword);

    /**
     * 根据手机查找用户
     * @param phone 手机号
     * @return
     */
    User findByPhone(String phone);

    /**
     * 根据邮箱查找账号
     * @param email 邮箱
     * @return
     */
    User findByEmail(String email);

    String login(String username, String password);

}
