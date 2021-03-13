package com.ning.modules.system.controller;

import com.ning.modules.security.TokenProvider;
import com.ning.modules.system.domain.Project;
import com.ning.modules.system.domain.ProjectGroup;
import com.ning.modules.system.domain.ProjectLog;
import com.ning.modules.system.service.ProjectGroupService;
import com.ning.modules.system.service.ProjectLogService;
import com.ning.modules.system.service.ProjectService;
import com.ning.modules.system.service.dto.ProjectPageDto;
import com.ning.utils.EntityExistException;
import com.ning.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
@Api(tags = "项目模块")
public class ProjectController {

    private final TokenProvider tokenProvider;
    private final ProjectService projectService;
    private final ProjectGroupService projectGroupService;
    private final ProjectLogService projectLogService;
    private final RedisUtils redisUtils;

    @PostMapping("/create")
    @ApiOperation("创建项目 ")
    public ResponseEntity<Object> createProject(@Validated @RequestBody Project project, HttpServletRequest request) {
        String username = tokenProvider.getUsername(tokenProvider.getToken(request));
        project.setCreateBy(username);
        if(project.getIntroduce().length() == 0) {
            project.setIntroduce("暂无简介内容");
        }
        projectService.create(project);
        Map<String, Object> res = new HashMap<String, Object>(1){{
            put("code", "200");
        }};
        return ResponseEntity.ok(res);
    }

    @GetMapping("/getMessage")
    @ApiOperation("获取当页信息")
    public ResponseEntity<Object> getInfo(ProjectPageDto projectPageDto, HttpServletRequest request){
        String username = tokenProvider.getUsername(tokenProvider.getToken(request));
        int total = projectService.getTotalCount(username, projectPageDto.getSearchCont());
        int startCount = (projectPageDto.getCurrentPage() - 1) * projectPageDto.getSize();
        List<Project> projectList = projectService.getInfo(startCount, projectPageDto.getSize(), username, projectPageDto.getSearchCont());
        Map<String, Object> res = new HashMap<String, Object>(3){{
            put("code", "200");
            put("total", total);
            put("rows", projectList);
        }};
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/deleteProject")
    @ApiOperation("删除项目")
    public ResponseEntity<Object> deleteProject(@RequestBody Project project, HttpServletRequest request) {
        projectService.deleteProject(project);
        Map<String, String> res = new HashMap<String, String>(1){{
            put("code", "200");
        }};
        return ResponseEntity.ok(res);
    }

    @GetMapping("/getMemberInfo")
    @ApiOperation("获取项目成员信息")
    public ResponseEntity<Object> getMemberInfo(HttpServletRequest request) {
        Long projectId = Long.valueOf(request.getParameter("projectId"));
//        List<UserInfo> users = projectUserService.findUsers(projectId);
        Map<String, Object> res = new HashMap<String, Object>(2){{
            put("code", "200");
//            put("users", users);
        }};
        return ResponseEntity.ok(res);
    }

    @GetMapping("/getGroupInfo")
    @ApiOperation("获取项目组信息")
    public ResponseEntity<Object> getGroupInfo(HttpServletRequest request) {
        Long projectId = Long.valueOf(request.getParameter("projectId"));
        List<ProjectGroup> groups = projectGroupService.findGroups(projectId);
        Map<String, Object> res = new HashMap<String, Object>(2){{
            put("code", "200");
            put("groups", groups);
        }};
        return ResponseEntity.ok(res);
    }

    @PostMapping("/createGroup")
    @ApiOperation("创建项目小组")
    public ResponseEntity<Object> createGroup(@Validated @RequestBody ProjectGroup group, HttpServletRequest request) {
        if(("").equals(group.getLeader())) {
            String username = tokenProvider.getUsername(tokenProvider.getToken(request));
            group.setLeader(username);
        }
        projectGroupService.create(group);
        Map<String, Object> res = new HashMap<String, Object>(1){{
            put("code", "201");
        }};
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @GetMapping("/getLog")
    @ApiOperation("获取项目日志")
    public ResponseEntity<Object> getLogs(@RequestParam("projectId") String projectId) {
        List<ProjectLog> logs = projectLogService.getLogs(Long.valueOf(projectId));
        Map<String, Object> res = new HashMap<String, Object>(2){{
            put("code", "200");
            put("logs", logs);
        }};
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/updateInfo")
    @ApiOperation("更新项目信息")
    public ResponseEntity<Object> updateProject(@RequestBody Project project) {
        projectService.updateProject(project);
        Map<String, Object> res = new HashMap<String, Object>(1){{
            put("code", "200");
        }};
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
