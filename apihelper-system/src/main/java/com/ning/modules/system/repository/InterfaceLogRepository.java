package com.ning.modules.system.repository;

import com.ning.modules.system.domain.InterfaceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfaceLogRepository extends JpaRepository<InterfaceLog, Long>, JpaSpecificationExecutor<InterfaceLog> {

    List<InterfaceLog> findAllByInterfaceName(String interfaceName);
}
