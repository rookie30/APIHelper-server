package com.ning.modules.system.repository;

import com.ning.modules.system.domain.MyInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InterfaceRepository extends JpaRepository<MyInterface, Long>, JpaSpecificationExecutor<MyInterface> {

    @Query(value = "select * from sys_interface where project = ?1", nativeQuery = true)
    List<MyInterface> findAllByProjectId(Long projectId);

    Integer countAllByNameAndProjectId(String interfaceName, Long projectId);

    MyInterface findByProjectIdAndCreateByAndCreateTime(Long projectId, String createBy, Date createTime);
}
