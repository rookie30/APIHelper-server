package com.ning.modules.system.repository;

import com.ning.modules.system.domain.ProjectLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectLogRepository extends JpaRepository<ProjectLog, Long>, JpaSpecificationExecutor<ProjectLog> {


}
