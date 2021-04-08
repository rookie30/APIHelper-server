package com.ning.modules.system.repository;

import com.ning.modules.system.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long>, JpaSpecificationExecutor<Notice> {

    Integer countByRecipient(String username);

    List<Notice> findAllByRecipient(String username);

    @Query(value = "select count(*) from sys_notice where create_by = ?1 and type = 1", nativeQuery = true)
    Integer findUnreadCount(String username);
}
