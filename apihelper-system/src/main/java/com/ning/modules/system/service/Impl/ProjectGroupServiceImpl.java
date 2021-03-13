package com.ning.modules.system.service.Impl;

import com.ning.modules.system.domain.ProjectGroup;
import com.ning.modules.system.repository.ProjectGroupRepository;
import com.ning.modules.system.service.ProjectGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectGroupServiceImpl implements ProjectGroupService {

    private final ProjectGroupRepository projectGroupRepository;

    @Override
    public List<ProjectGroup> findGroups(Long projectId) {
        return projectGroupRepository.findGroups(projectId);
    }

    @Override
    public void create(ProjectGroup group) {
        projectGroupRepository.save(group);
    }
}
