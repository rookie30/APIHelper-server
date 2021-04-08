package com.ning.modules.system.service.Impl;

import com.ning.modules.config.RsaProperties;
import com.ning.modules.security.TokenProvider;
import com.ning.modules.security.dto.UserDto;
import com.ning.modules.security.service.UserDetailsServiceImpl;
import com.ning.modules.system.domain.Role;
import com.ning.modules.system.domain.User;
import com.ning.modules.system.repository.RoleRepository;
import com.ning.modules.system.repository.UserRepository;
import com.ning.modules.system.service.RoleService;
import com.ning.modules.system.service.UserService;
import com.ning.utils.EntityExistException;
import com.ning.utils.RsaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final TokenProvider tokenProvider;
    private final RoleRepository roleRepository;

    @Override
    public void create(User resource) {
        if(userRepository.findByUsername(resource.getUsername()) != null) {
            throw new EntityExistException("用户名", resource.getUsername());
        }
        if(userRepository.findByPhone(resource.getPhone()) != null) {
            throw new EntityExistException("手机号", resource.getPhone());
        }
        if(userRepository.findByEmail(resource.getEmail()) != null) {
            throw new EntityExistException("邮箱", resource.getEmail());
        }
        try {
            resource.setPassword(new BCryptPasswordEncoder().encode(resource.getPassword()));
            resource.setUpdateBy(resource.getUsername());
            // 新创建的用户默认权限等级为0
            Role role = roleRepository.findByLevel(0);
            if(role.getUsers() == null) {
                role.setUsers(new HashSet<>());
            }
            role.getUsers().add(resource);
            resource.setRoles(new HashSet<>());
            resource.getRoles().add(role);
            roleRepository.save(role);
            userRepository.save(resource);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(User resource) {
        String username = resource.getUsername();
        String nickname = resource.getNickname();
        String phone = resource.getPhone();
        String email = resource.getEmail();
        int gender = resource.getGender();
        Date updateTime = new Date();
        userRepository.update(username, nickname, phone, email, gender, updateTime);
    }

    @Override
    public User findByName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void updateEmail(String username, String email) {

    }

    @Override
    public void delete(Set<Long> ids) {

    }

    @Override
    public void updatePass(String username, String encryptPassword) {
        userRepository.updatePass(username, encryptPassword, new Date());
    }

    @Override
    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = tokenProvider.createToken(userDetails.getUsername());
        return token;
    }

}
