package com.ning.modules.system.controller;

import com.ning.modules.security.TokenProvider;
import com.ning.modules.system.domain.Project;
import com.ning.modules.system.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping("/getAll")
    @ApiOperation("获取参与的所有项目信息")
    public ResponseEntity<Object> getAll(HttpServletRequest request) {
        String username = tokenProvider.getUsername(tokenProvider.getToken(request));
        List<Project> projectList = projectService.findAll(username);
        Map<String, Object> res = new HashMap<String, Object>(2){{
            put("code", "200");
            put("projectList", projectList);
        }};
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
