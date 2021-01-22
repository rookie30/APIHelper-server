package com.ning.modules.system.repository;

import com.ning.modules.system.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * 根据账号查询
     * @param username 账号
     * @return
     */
    User findByUsername(String username);

    /**
     * 根据手机号查询
     * @param phone 手机号
     * @return
     */
    User findByPhone(String phone);

    /**
     * 根据邮箱查询
     * @param email
     * @return
     */
    User findByEmail(String email);


}
