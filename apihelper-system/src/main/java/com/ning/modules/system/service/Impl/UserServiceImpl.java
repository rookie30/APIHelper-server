package com.ning.modules.system.service.Impl;

import com.ning.modules.config.RsaProperties;
import com.ning.modules.security.TokenProvider;
import com.ning.modules.security.dto.UserDto;
import com.ning.modules.security.service.UserDetailsServiceImpl;
import com.ning.modules.system.domain.Role;
import com.ning.modules.system.domain.User;
import com.ning.modules.system.repository.UserRepository;
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

import java.util.HashSet;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final TokenProvider tokenProvider;

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
//            String encryptedPassword = RsaUtils.encryptByPrivateKey(RsaProperties.privateKey, resource.getPassword());
            resource.setPassword(new BCryptPasswordEncoder().encode(resource.getPassword()));
            resource.setUpdateBy(resource.getUsername());
            Set<Role> roles = new HashSet<>();
            Role role = new Role();
            userRepository.save(resource);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(User resource) {

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
