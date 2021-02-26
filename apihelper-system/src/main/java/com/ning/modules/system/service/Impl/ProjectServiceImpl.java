package com.ning.modules.system.service.Impl;

import com.ning.modules.security.TokenProvider;
import com.ning.modules.system.domain.Project;
import com.ning.modules.system.domain.ProjectLog;
import com.ning.modules.system.repository.ProjectRepository;
import com.ning.modules.system.service.ProjectLogService;
import com.ning.modules.system.service.ProjectService;
import com.ning.utils.EntityExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectLogService projectLogService;

    @Override
    public void create(Project project) {
        if(projectRepository.findByProjectName(project.getProjectName()) != null) {
            throw new EntityExistException("项目", project.getProjectName());
        }
        project.setCreateTime(new Date());
        projectRepository.save(project);
        long projectId = project.getId();
        String projectName = project.getProjectName();
        String operator = project.getCreateBy();
        Date createTime = project.getCreateTime();
        String content = operator + " 于 " + createTime + " 创建了项目 " + projectName;
        ProjectLog projectLog = new ProjectLog(operator, content, projectId);
        projectLogService.addLog(projectLog);
    }

    @Override
    public int getTotalCount(String username) {
        return projectRepository.countAllByCreateBy(username);
    }
}
