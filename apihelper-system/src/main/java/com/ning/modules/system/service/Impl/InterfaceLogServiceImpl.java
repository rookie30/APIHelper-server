package com.ning.modules.system.service.Impl;

import com.ning.modules.system.domain.InterfaceLog;
import com.ning.modules.system.repository.InterfaceLogRepository;
import com.ning.modules.system.service.InterfaceLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterfaceLogServiceImpl implements InterfaceLogService {

    private final InterfaceLogRepository interfaceLogRepository;

    @Override
    public void add(InterfaceLog log) {
        interfaceLogRepository.save(log);
    }

    @Override
    public List<InterfaceLog> findAllByInterfaceId(Long interfaceId) {
        return interfaceLogRepository.findAllByInterfaceId(interfaceId);
    }
}
