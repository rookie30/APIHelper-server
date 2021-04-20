package com.ning.modules.system.service.Impl;

import com.ning.modules.system.domain.Project;
import com.ning.modules.system.domain.ProjectLog;
import com.ning.modules.system.domain.User;
import com.ning.modules.system.domain.vo.UserInfo;
import com.ning.modules.system.repository.ProjectRepository;
import com.ning.modules.system.repository.UserRepository;
import com.ning.modules.system.service.NoticeService;
import com.ning.modules.system.service.ProjectLogService;
import com.ning.modules.system.service.ProjectService;
import com.ning.modules.system.service.UserService;
import com.ning.utils.BadRequestException;
import com.ning.utils.EntityExistException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.RuntimeErrorException;
import java.text.DateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectLogService projectLogService;
    private final DateFormat dateFormat = DateFormat.getDateTimeInstance();
    private final UserService userService;
    private final UserRepository userRepository;
    private final NoticeService noticeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Project project) {
        if(projectRepository.findByProjectName(project.getProjectName(), project.getCreateBy()) != null) {
            throw new EntityExistException("项目", project.getProjectName());
        }
        project.setCreateTime(new Date());
        // 添加project-user关联表信息
        User user = userService.findByName(project.getCreateBy());
        if(user.getProjects() == null) {
            user.setProjects(new HashSet<>());
        }
        user.getProjects().add(project);
        project.setUsers(new HashSet<>());
        project.getUsers().add(user);
        projectRepository.save(project);
        // 添加日志
        Long projectId = project.getId();
        String projectName = project.getProjectName();
        String username = project.getCreateBy();
        Date createTime = project.getCreateTime();
        String content = username + " 于 " + dateFormat.format(createTime) + " 创建了项目 " + projectName;
        String type = "create";
        ProjectLog projectLog = new ProjectLog(username, content, projectId, type);
        projectLogService.addLog(projectLog);
    }

    @Override
    public Integer getTotalCount(String username, String searchCont) {
        return projectRepository.countAllBySearchContLike(username, searchCont);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProject(Project project) {
        String username = project.getCreateBy();
        String projectName = project.getProjectName();
        Project projectInfo = projectRepository.findByProjectName(projectName, username);
        // 删除关联表中的信息
        User user = userService.findByName(username);
        Set<Project> projects = user.getProjects();
        projects.remove(projectRepository.findByProjectName(projectName, username));
        // 删除项目
        projectRepository.deleteProject(username, projectName);
        // 添加日志
        String content = username + " 于 " + dateFormat.format(new Date()) + " 删除了项目 " + projectName;
        String logType = "delete";
        ProjectLog projectLog = new ProjectLog(username, content, projectInfo.getId(), logType);
        projectLogService.addLog(projectLog);
    }

    @Override
    public List<Project> getInfo(Integer startCount, Integer size, String username, String searchCont) {
        return projectRepository.pageQuery(startCount, size, username, searchCont);
    }

    @Override
    @ApiOperation("获取所有项目")
    public List<Project> findAll(String username) {
        User user = userService.findByName(username);
        return new ArrayList<>(user.getProjects());
    }

    @Override
    @ApiOperation("更新项目信息")
    @Transactional(rollbackFor = Exception.class)
    public void updateProject(Project newProject) {
        String username = newProject.getCreateBy();
        Date createTime = newProject.getCreateTime();
        Project oldProject = projectRepository.findByCreateByAndCreateTime(username, createTime);
        Date updateTime = new Date();
        String logType = "update";
        List<ProjectLog> logs = new ArrayList<>();
        if(!oldProject.getProjectName().equals(newProject.getProjectName())) {
            // 判断更新后的项目名是否存在
            Project sameNameProject = projectRepository.findByProjectName(newProject.getProjectName(), newProject.getCreateBy());
            if(sameNameProject != null) {
                throw new EntityExistException("项目名", newProject.getProjectName());
            }
            oldProject.setProjectName(newProject.getProjectName());
            // 添加日志
            String nameLogCont = oldProject.getCreateBy() + " 于 "
                                + dateFormat.format(updateTime)
                                + " 将原项目名 " + oldProject.getProjectName()
                                + " 更新为 " + newProject.getProjectName();
            ProjectLog nameLog = new ProjectLog(oldProject.getCreateBy(), nameLogCont, oldProject.getId(), logType);
            logs.add(nameLog);
        }
        if(!oldProject.getIntroduce().equals(newProject.getIntroduce())) {
            oldProject.setIntroduce(newProject.getIntroduce());
            String introduceLogCont = oldProject.getCreateBy() + " 于 "
                                + dateFormat.format(updateTime)
                                + " 更新了项目 " + newProject.getProjectName()
                                + " 的简介内容";
            ProjectLog introduceLog = new ProjectLog(oldProject.getCreateBy(), introduceLogCont, oldProject.getId(), logType);
            logs.add(introduceLog);
        }
        projectLogService.addLogs(logs);
        projectRepository.save(oldProject);
    }

    @Override
    public Project findById(Long projectId) {
        return projectRepository.findByProjectId(projectId);
    }

    @Override
    public List<UserInfo> getMembers(Long projectId) {
        Project project = this.findById(projectId);
        List<User> entireUserInfo = new ArrayList<>(project.getUsers());
        List<UserInfo> users = new ArrayList<>();
        for (int i = 0; i < entireUserInfo.size(); i++) {
            User userItem = entireUserInfo.get(i);
            String username = userItem.getUsername();
            String nickname = userItem.getNickname();
            String email = userItem.getEmail();
            String phone = userItem.getPhone();
            Integer gender = userItem.getGender();
            users.add(new UserInfo(username, nickname, email, phone, gender));
        }
        return users;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inviteMember(String operator, String username, Long projectId) {
        User user = userService.findByName(username);
        if(user == null) {
            throw new BadRequestException("用户不存在");
        }
        Project project = projectRepository.findByProjectId(projectId);
        Set<Project> projects = user.getProjects();
        if(projects.contains(project)) {
            throw new EntityExistException("成员", user.getUsername());
        }
        if(user.getProjects() == null) {
            user.setProjects(new HashSet<>());
        }
        user.getProjects().add(project);
        project.getUsers().add(user);
        projectRepository.save(project);
        // 创建消息
        String title = "项目邀请";
        String content = "您已被用户 " + operator + " 邀请加入了项目 " +
                        project.getProjectName();
        noticeService.addNotice(title, content, operator, username);
        // 创建日志
        String logCont = "管理员 " + operator + " 于 " + dateFormat.format(new Date())
                        + " 邀请了用户 " + username + " 加入了此项目";
        String logType = "invite";
        ProjectLog log = new ProjectLog(operator, logCont, projectId, logType);
        projectLogService.addLog(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMember(String operator, String username, Long projectId) {
        User user = userService.findByName(username);
        Project project = projectRepository.findByProjectId(projectId);
        user.getProjects().remove(project);
        project.getUsers().remove(user);
        projectRepository.save(project);
        // 创建消息
        String title = "您已被移出项目";
        String content = "您已被 " + operator + " 移出项目 " +
                project.getProjectName();
        noticeService.addNotice(title, content, operator, username);
        // 创建日志
        String logCont = "用户 " + username + " 于 " + dateFormat.format(new Date())
                + " 被管理员 " + operator + " 移出项目";
        String logType = "remove";
        ProjectLog log = new ProjectLog(operator, logCont, projectId, logType);
        projectLogService.addLog(log);
    }
}
