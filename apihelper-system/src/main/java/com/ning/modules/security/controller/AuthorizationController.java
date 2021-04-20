package com.ning.modules.security.controller;

import cn.hutool.core.util.IdUtil;
import com.github.javafaker.Faker;
import com.ning.modules.config.LoginCodeEnum;
import com.ning.modules.config.LoginProperties;
import com.ning.modules.config.RsaProperties;
import com.ning.modules.config.SecurityProperties;
import com.ning.modules.security.TokenProvider;
import com.ning.modules.security.dto.AuthUserDto;
import com.ning.modules.security.dto.JwtUserDto;
import com.ning.modules.security.service.UserDetailsServiceImpl;
import com.ning.modules.system.domain.Role;
import com.ning.modules.system.domain.User;
import com.ning.modules.system.repository.RoleRepository;
import com.ning.modules.system.service.Impl.RoleServiceImpl;
import com.ning.modules.system.service.Impl.UserServiceImpl;
import com.ning.modules.system.service.UserService;
import com.ning.utils.BadRequestException;
import com.ning.utils.RedisUtils;
import com.ning.utils.RsaUtils;
import com.ning.utils.StringUtils;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(tags = "系统：系统授权接口")
public class AuthorizationController {
    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    @Resource
    private LoginProperties loginProperties;
    private final UserService userService;
    // 测试模块
    private final RoleServiceImpl roleService;
    private final UserDetailsServiceImpl userDetailsService;


    @ApiOperation("登录授权")
    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@Validated @RequestBody AuthUserDto authUserDto) throws Exception {
        // 密码解密
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUserDto.getPassword());
        String username = authUserDto.getUsername();
        // 查询验证码
        String code = (String) redisUtils.get(authUserDto.getUuid());
        // 清除验证码
        redisUtils.del(authUserDto.getUuid());
        if (StringUtils.isBlank(code)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(authUserDto.getCode()) || !authUserDto.getCode().equalsIgnoreCase(code)) {
            throw new BadRequestException("验证码错误");
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌
        String token = tokenProvider.createToken(authUserDto.getUsername());
        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", jwtUserDto);
        }};
        return ResponseEntity.ok(authInfo);
    }

    @ApiOperation("获取验证码")
    @GetMapping(value = "/code")
    public ResponseEntity<Object> getCode() {
        // 获取运算的结果
        Captcha captcha = loginProperties.getCaptcha();
        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == LoginCodeEnum.arithmetic.ordinal() && captchaValue.contains(".")) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisUtils.set(uuid, captchaValue, loginProperties.getLoginCode().getExpiration(), TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return ResponseEntity.ok(imgResult);
    }

    @ApiOperation("退出登录")
    @DeleteMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        String key = properties.getOnlineKey() + tokenProvider.getToken(request);
        redisUtils.del(key);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping("/test")
    public ResponseEntity<Object> test() {
        WebClient client = WebClient.create("http://47.111.151.229");
        //定义url参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("year", "2021");
        params.add("month", "4");
        params.add("day", "10");
        String uri = UriComponentsBuilder.fromUriString("/api/user/getLoginNumber")
                .queryParams(params)
                .toUriString();
        Mono<String> result = client.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class);
        Map<String, Object> res = new HashMap<>();
        res.put("result", result.block());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/test1")
    public ResponseEntity<Object> test1(HttpServletRequest request) {
        String account = request.getParameter("account");
        Map<String, Object> res = new HashMap<>();
        res.put("status", 200);
        res.put("account", account);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/test2")
    public ResponseEntity<Object> test2(@RequestBody Map<String, Object> params) {
        Map<String, Object> res = new HashMap<>();
        res.put("status", "200");
        res.put("params", params);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/test3")
    public ResponseEntity<Object> test3() {
        return new ResponseEntity<>(new Date(), HttpStatus.OK);
    }


}
