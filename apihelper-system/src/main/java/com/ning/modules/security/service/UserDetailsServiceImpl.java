package com.ning.modules.security.service;

import com.ning.modules.security.dto.JwtUserDto;
import com.ning.modules.system.domain.User;
import com.ning.modules.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if(user==null){
            // 实际当用户不存在时，应该页面显示错误信息，并跳转到登录界面
            throw new UsernameNotFoundException("该用户不存在！");
        }
//        user.setRoles(userRepository.findByUserId(username));
        return new JwtUserDto(user);
    }

}
