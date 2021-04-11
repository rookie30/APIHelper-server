package com.ning.modules.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.javafaker.Faker;
import com.ning.modules.security.TokenProvider;
import com.ning.modules.system.domain.InterfaceLog;
import com.ning.modules.system.domain.MyInterface;
import com.ning.modules.system.service.InterfaceLogService;
import com.ning.modules.system.service.InterfaceService;
import com.ning.modules.system.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/interface")
@RequiredArgsConstructor
@Api(tags = "接口管理模块")
public class InterfaceController {

    private final TokenProvider tokenProvider;
    private final ProjectService projectService;
    private final InterfaceService interfaceService;
    private final InterfaceLogService interfaceLogService;

    private Faker faker = new Faker();

    @GetMapping("/getInfo")
    @ApiOperation("获取项目中的所有接口信息")
    public ResponseEntity<Object> getInfo(HttpServletRequest request) {
        Long projectId = Long.valueOf(request.getParameter("id"));
        List<MyInterface> interfaceList = interfaceService.getByProjectId(projectId);
        Map<String, Object> res = new HashMap<String, Object>(2){{
            put("status", "200");
            put("interfaceList", interfaceList);
        }};
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/create")
    @ApiOperation("创建接口")
    public ResponseEntity<Object> createInterface(@RequestBody MyInterface myInterface, HttpServletRequest request) {
        Map<String, Object> res = new HashMap<>();
        String username = tokenProvider.getUsername(tokenProvider.getToken(request));
        myInterface.setUpdateBy(username);
        interfaceService.create(myInterface);
        res.put("status", "201");
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @GetMapping("/getLog")
    @ApiOperation("获取接口日志")
    public ResponseEntity<Object> getLog(HttpServletRequest request) {
        Long interfaceId = Long.valueOf(request.getParameter("interfaceId"));
        List<InterfaceLog> interfaceLogs = interfaceLogService.findAllByInterfaceId(interfaceId);
        Map<String, Object> res = new HashMap<String, Object>(2) {{
            put("status", "200");
            put("logData", interfaceLogs);
        }};
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/update")
    @ApiOperation("更新接口信息")
    public ResponseEntity<Object> updateInterface(@RequestBody MyInterface newInterface, HttpServletRequest request) {
        Map<String, Object> res = new HashMap<>();
        String username = tokenProvider.getUsername(tokenProvider.getToken(request));
        newInterface.setUpdateBy(username);
        interfaceService.update(newInterface);
        res.put("status", "200");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/manualTest")
    @ApiOperation("手动测试")
    public ResponseEntity<Object> manualTest(@RequestBody MyInterface myInterface) {
        Mono<String> result = interfaceService.manualTest(myInterface);
        Map<String, Object> res = new HashMap<>();
        try {
            String blockResult = result.block();
            res.put("data", blockResult);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (RuntimeException err) {
            res.put("data", err.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/autoTest")
    @ApiOperation("自动测试")
    public ResponseEntity<Object> autoTest(@RequestBody MyInterface myInterface) {
        Map<String, Object> res = new HashMap<>();
        List<Map<String, Object>> testResult = interfaceService.autoTest(myInterface);
        res.put("data", testResult);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}