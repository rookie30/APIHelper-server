package com.ning.modules.security.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ning.modules.system.domain.Role;
import com.ning.modules.system.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Proxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@AllArgsConstructor
public class JwtUserDto implements UserDetails {

    private final User user;

//    public JwtUserDto(User user) {
//        this.user = user;
//    }


    @JSONField(serialize = false)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> userRoles = user.getRoles();
        List<GrantedAuthority> auths = new ArrayList<>(userRoles.size());
        userRoles.parallelStream().forEach(userRole -> {
            // 默认ROLE_  为前缀，可以更改
            auths.add(new SimpleGrantedAuthority("ROLE_" + userRole.getName()));
        });
        return auths;
    }

    @JSONField(serialize = false)
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @JSONField(serialize = false)
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 账户是否未过期
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账户是否未锁定
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 密码是否未过期
    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 账户是否激活
    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return true;
    }
}
