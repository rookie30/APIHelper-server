package com.ning.modules.system.service;

import com.ning.modules.system.domain.MyInterface;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface InterfaceService {

    List<MyInterface> getByProjectId(Long projectId);

    void create(MyInterface myInterface);

    Boolean checkIfExisted(String interfaceName, Long projectId);

    void update(MyInterface newInterface);

    Mono<String> manualTest(MyInterface myInterface);

    List<Map<String, Object>> autoTest(MyInterface myInterface);
}
