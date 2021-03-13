package com.ning.modules.system.service;

import com.ning.modules.system.domain.ProjectLog;

import java.util.List;

public interface ProjectLogService {

    void addLog(ProjectLog projectLog);

    void addLogs(List<ProjectLog> logs);

    List<ProjectLog> getLogs(Long id);


}
