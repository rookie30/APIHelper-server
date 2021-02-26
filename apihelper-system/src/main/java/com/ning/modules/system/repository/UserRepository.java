package com.ning.modules.system.repository;

import com.ning.modules.system.domain.Role;
import com.ning.modules.system.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Set;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

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

    /**
     * 更新密码
     * @param username 账号
     * @param newPassword 新密码
     * @param updateTime 更新时间
     */
    @Modifying
    @Query(value = "update sys_user set password = ?2, update_time = ?3 where username = ?1", nativeQuery = true)
    void updatePass(String username, String newPassword, Date updateTime);

    /**
     * 更新个人信息
     * @param username 账号
     * @param nickname 昵称
     * @param phone 手机号
     * @param email 邮箱
     * @param gender 性别
     * @param updateTime 更新时间
     */
    @Modifying
    @Query(value = "update sys_user set nickname = ?2, phone = ?3, email = ?4, gender = ?5, " +
            "update_time = ?6 where username = ?1", nativeQuery = true)
    void update(String username, String nickname, String phone, String email, int gender, Date updateTime);

}
