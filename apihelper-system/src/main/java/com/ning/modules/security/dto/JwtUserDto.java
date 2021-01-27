package com.ning.modules.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ning.modules.system.domain.Role;
import com.ning.modules.system.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
public class JwtUserDto implements UserDetails {

    private User user;

    public JwtUserDto(User user) {
        this.user = user;
    }

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

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 账户是否未过期
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账户是否未锁定
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 密码是否未过期
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 账户是否激活
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
