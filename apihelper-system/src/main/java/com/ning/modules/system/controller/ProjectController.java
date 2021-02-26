package com.ning.modules.system.controller;

import com.ning.modules.security.TokenProvider;
import com.ning.modules.system.domain.Project;
import com.ning.modules.system.service.ProjectService;
import com.ning.modules.system.service.dto.ProjectPageDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
@Api(tags = "项目模块")
public class ProjectController {

    private final TokenProvider tokenProvider;
    private final ProjectService projectService;

    @PostMapping("/create")
    @ApiOperation("创建项目 ")
    public ResponseEntity<Object> createProject(@Validated @RequestBody Project project, HttpServletRequest request) {
        String username = tokenProvider.getUsername(tokenProvider.getToken(request));
        project.setCreateBy(username);
        projectService.create(project);

        Map<String, String> res = new HashMap<String, String>(1){{
            put("code", "200");
        }};
        return ResponseEntity.ok(res);
    }

    @GetMapping("/getTotalCount")
    @ApiOperation("获取所有项目数量")
    public ResponseEntity<Object> getTotalCount(HttpServletRequest request) {
        String username = tokenProvider.getUsername(tokenProvider.getToken(request));
        int count = projectService.getTotalCount(username);
        Map<String, String> res = new HashMap<String, String>(2){{
            put("code", "200");
            put("count", String.valueOf(count));
        }};
        return ResponseEntity.ok(res);
    }

    @GetMapping("/getMessage")
    @ApiOperation("获取当页信息")
    public ResponseEntity<Object> getInfo(ProjectPageDto projectPageDto, HttpServletRequest request){
        String username = tokenProvider.getUsername(tokenProvider.getToken(request));
//        int totalCount = projectService.getTotalCount();
        Map<String, Object> res = new HashMap<String, Object>(2){{
            put("code", "200");
            put("project", projectPageDto);
        }};
        return ResponseEntity.ok(res);
    }

//    @DeleteMapping("/deleteProject")
//    @ApiOperation("删除项目")
//    public ResponseEntity<Object> deleteProject(HttpServletRequest request) {
//
//    }

}
