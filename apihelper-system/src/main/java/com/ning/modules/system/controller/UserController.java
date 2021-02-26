package com.ning.modules.system.controller;

import com.ning.modules.config.SecurityProperties;
import com.ning.modules.security.TokenProvider;
import com.ning.modules.system.domain.User;
import com.ning.modules.system.domain.vo.UserPassVo;
import com.ning.modules.system.service.UserService;
import com.ning.utils.BadRequestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Api(tags = "用户模块")
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public ResponseEntity<Object> register(@Validated @RequestBody User resource) {
        userService.create(resource);
        Map<String, String> result = new HashMap<>();
        result.put("code", "201");
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/userInfo")
    public ResponseEntity<Object> query(HttpServletRequest request) {
        String token = tokenProvider.getToken(request);
        String username = tokenProvider.getUsername(token);
        User user = userService.findByName(username);
        return ResponseEntity.ok(user);
    }

    @ApiOperation("修改密码")
    @PostMapping("/changePwd")
    public ResponseEntity<Object> changePassword(@RequestBody UserPassVo userPassVo, HttpServletRequest request) {
        String username = tokenProvider.getUsername(tokenProvider.getToken(request));
        User user = userService.findByName(username);
        String oldPassword = user.getPassword();
        String inputOldPassword = userPassVo.getOldPassword();
        String newPassword = userPassVo.getNewPassword();
        boolean passwordIsMatching = new BCryptPasswordEncoder().matches(inputOldPassword, oldPassword);
        if(!passwordIsMatching) {
            throw new BadRequestException("密码错误");
        }
        if(new BCryptPasswordEncoder().matches(newPassword, oldPassword)) {
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        userService.updatePass(username, new BCryptPasswordEncoder().encode(newPassword));
        Map<String, String> res = new HashMap<String, String>(1){{
            put("code", "200");
        }};
        return ResponseEntity.ok(res);
    }

    @ApiOperation("编辑个人信息")
    @PostMapping("/editInfo")
    public ResponseEntity<Object> editInfo(@Validated @RequestBody User user, HttpServletRequest request) {
        String username = tokenProvider.getUsername(tokenProvider.getToken(request));
        if(!user.getUsername().equals(username)) {
            throw new BadRequestException("无法修改其他用户");
        }
        userService.update(user);
        Map<String, String> res = new HashMap<String, String>(1){{
            put("code", "200");
        }};
        return ResponseEntity.ok(res);
    }

}
