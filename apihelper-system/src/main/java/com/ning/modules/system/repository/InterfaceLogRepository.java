package com.ning.modules.system.repository;

import com.ning.modules.system.domain.InterfaceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfaceLogRepository extends JpaRepository<InterfaceLog, Long>, JpaSpecificationExecutor<InterfaceLog> {

    @Query(value = "select * from sys_interface_log where interface_id = ?1 ", nativeQuery = true)
    List<InterfaceLog> findAllByInterfaceId(Long interfaceId);
}
