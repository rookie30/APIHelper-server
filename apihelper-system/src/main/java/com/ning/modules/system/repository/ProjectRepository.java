package com.ning.modules.system.repository;

import com.ning.modules.system.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    Project findByProjectName(String name);

    /**
     * 根据创建用户查询该用户创建的所有项目数量
     * @param username
     * @return
     */
    @Query(value = "select count(1) from sys_project where create_by = ?1", nativeQuery = true)
    int countAllByCreateBy(String username);

//    @Query(value = "delete from ")
//    void deleteProjectByCreateBy(String username);


}
