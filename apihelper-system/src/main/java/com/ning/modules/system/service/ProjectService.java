package com.ning.modules.system.service;


import com.ning.modules.system.domain.Project;
import org.springframework.stereotype.Service;

public interface ProjectService {

    /**
     * 创建项目
     * @param project
     */
    void create(Project project);

    int getTotalCount(String username);
}
