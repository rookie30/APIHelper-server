package com.ning.modules.system.service;


import com.ning.modules.system.domain.Project;
import com.ning.modules.system.domain.ProjectGroup;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProjectService {

    /**
     * 创建项目
     * @param project
     */
    void create(Project project);

    Integer getTotalCount(String username, String searchCont);

    void deleteProject(Project project);

    List<Project> getInfo(Integer startCount, Integer size, String username, String searchCont);

    List<Project> findAll(String username);

    void updateProject(Project newProject);

}
