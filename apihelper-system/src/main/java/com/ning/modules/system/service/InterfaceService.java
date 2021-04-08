package com.ning.modules.system.service;

import com.ning.modules.system.domain.MyInterface;

import java.util.List;

public interface InterfaceService {

    List<MyInterface> getByProjectId(Long projectId);

    void create(MyInterface myInterface);

    Boolean checkIfExisted(String interfaceName, Long projectId);

    void update(MyInterface newInterface);
}
