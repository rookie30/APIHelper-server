package com.ning.modules.system.service.Impl;

import com.ning.modules.system.domain.ProjectLog;
import com.ning.modules.system.repository.ProjectLogRepository;
import com.ning.modules.system.service.ProjectLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectLogServiceImpl implements ProjectLogService {

    private final ProjectLogRepository projectLogRepository;

    @Override
    public void addLog(ProjectLog projectLog) {
        projectLogRepository.save(projectLog);
    }
}
