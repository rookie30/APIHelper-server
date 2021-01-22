package com.ning.modules.system.service.Impl;

import com.ning.modules.config.RsaProperties;
import com.ning.modules.security.dto.UserDto;
import com.ning.modules.system.pojo.User;
import com.ning.modules.system.repository.UserRepository;
import com.ning.modules.system.service.UserService;
import com.ning.utils.EntityExistException;
import com.ning.utils.RsaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
            String encryptedPassword = RsaUtils.encryptByPrivateKey(RsaProperties.privateKey, resource.getPassword());
            resource.setPassword(encryptedPassword);
            resource.setUpdateBy(resource.getUsername());
            userRepository.save(resource);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(User resource) {

    }

    @Override
    public UserDto findByName(String username) {
        return null;
    }

    @Override
    public void updateEmail(String username, String email) {

    }

    @Override
    public void updatePassword(String username, String password) {

    }
}
