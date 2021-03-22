package com.ning.modules.system.service;

import com.ning.modules.system.domain.InterfaceLog;

import java.util.List;

public interface InterfaceLogService {

    void add(InterfaceLog log);

    List<InterfaceLog> findAllByInterfaceName(String interfaceName);
}
