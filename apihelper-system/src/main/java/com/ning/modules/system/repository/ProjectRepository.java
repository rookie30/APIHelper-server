package com.ning.modules.system.repository;

import com.ning.modules.system.domain.Project;
import com.ning.modules.system.domain.ProjectGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    @Query(value = "select * from sys_project where name = ?1 and create_by = ?2", nativeQuery = true)
    Project findByProjectName(String projectName, String username);

    /**
     * 根据创建用户查询该用户创建的所有项目数量
     * @param username
     * @return
     */
    @Query(value = "select count(1) from sys_project where create_by = ?1", nativeQuery = true)
    Integer countAllByCreateBy(String username);

    @Modifying
    @Query(value = "delete from sys_project where create_by = ?1 and  name = ?2", nativeQuery = true)
    void deleteProject(String username, String projectName);

    @Query(value = "select count(1) from sys_project where create_by = ?1 and name like %?2%", nativeQuery = true)
    Integer countAllBySearchContLike(String username, String searchCont);

    @Query(value = "select * from sys_project where create_by = ?3 and name like %?4% limit ?1, ?2", nativeQuery = true)
    List<Project> pageQuery(Integer startCount, Integer size, String username, String searchCont);

    @Query(value = "select name, id from sys_project s1, sys_user s2 where username = ?1", nativeQuery = true)
    List<Project> findAllByCreateBy(String username);

    Project findByCreateByAndCreateTime(String username, Date createTime);
}
