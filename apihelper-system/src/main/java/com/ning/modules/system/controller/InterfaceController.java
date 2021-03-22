package com.ning.modules.system.controller;

import com.ning.modules.security.TokenProvider;
import com.ning.modules.system.domain.InterfaceLog;
import com.ning.modules.system.domain.MyInterface;
import com.ning.modules.system.domain.Project;
import com.ning.modules.system.service.InterfaceLogService;
import com.ning.modules.system.service.InterfaceService;
import com.ning.modules.system.service.ProjectService;
import com.ning.utils.EntityExistException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interface")
@RequiredArgsConstructor
@Api(tags = "接口管理模块")
public class InterfaceController {

    private final TokenProvider tokenProvider;
    private final ProjectService projectService;
    private final InterfaceService interfaceService;
    private final InterfaceLogService interfaceLogService;

    @GetMapping("/getAll")
    @ApiOperation("获取参与的所有项目信息")
    public ResponseEntity<Object> getAll(HttpServletRequest request) {
        String username = tokenProvider.getUsername(tokenProvider.getToken(request));
        List<Project> projectList = projectService.findAll(username);
        Map<String, Object> res = new HashMap<String, Object>(2){{
            put("status", "200");
            put("projectList", projectList);
        }};
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

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
    public ResponseEntity<Object> create(@RequestBody MyInterface myInterface, HttpServletRequest request) {
        // 判断项目中是否已存在该接口名
        String interfaceName = myInterface.getName();
        Long projectId = myInterface.getProjectId();
        Boolean isAlreadyExist = interfaceService.checkIfExisted(interfaceName, projectId);
        Map<String, Object> res = new HashMap<>();
        if(isAlreadyExist) {
            throw new EntityExistException("接口名", interfaceName);
//            res.put("status", "500");
//            res.put("message", "接口名已存在");
//            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        String username = tokenProvider.getUsername(tokenProvider.getToken(request));
        myInterface.setUpdateBy(username);
        interfaceService.create(myInterface);
        res.put("status", "201");
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @GetMapping("/getLog")
    @ApiOperation("获取接口日志")
    public ResponseEntity<Object> getLog(HttpServletRequest request) {
        String interfaceName = request.getParameter("interfaceName");
        List<InterfaceLog> interfaceLogs = interfaceLogService.findAllByInterfaceName(interfaceName);
        Map<String, Object> res = new HashMap<String, Object>(2) {{
            put("status", "200");
            put("logData", interfaceLogs);
        }};
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}