package com.ning.modules.system.service;

import com.ning.modules.system.domain.ProjectGroup;

import java.util.List;

public interface ProjectGroupService {

    List<ProjectGroup> findGroups(Long projectId);

    void create(ProjectGroup group);
}
