package com.ning.modules.system.repository;

import com.ning.modules.system.domain.ProjectGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ProjectGroupRepository extends JpaRepository<ProjectGroup, Long>, JpaSpecificationExecutor<ProjectGroup> {

    @Query(value = "select * from sys_group where project = ?1", nativeQuery = true)
    List<ProjectGroup> findGroups(Long projectId);


}
