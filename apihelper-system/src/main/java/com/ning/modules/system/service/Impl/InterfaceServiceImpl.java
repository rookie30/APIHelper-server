package com.ning.modules.system.service.Impl;

import com.ning.modules.system.domain.InterfaceLog;
import com.ning.modules.system.domain.MyInterface;
import com.ning.modules.system.repository.InterfaceRepository;
import com.ning.modules.system.service.InterfaceLogService;
import com.ning.modules.system.service.InterfaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterfaceServiceImpl implements InterfaceService {

    private final InterfaceRepository interfaceRepository;
    private final DateFormat dateFormat = DateFormat.getDateTimeInstance();
    private final InterfaceLogService interfaceLogService;

    @Override
    public List<MyInterface> getByProjectId(Long projectId) {
        return interfaceRepository.findAllByProjectId(projectId);
    }

    @Override
    public void create(MyInterface myInterface) {
        Date createTime = interfaceRepository.save(myInterface).getCreateTime();
        // 记录日志
        String user = myInterface.getCreateBy();
        String interfaceName = myInterface.getName();
        String content = user + " 于 "+ dateFormat.format(createTime)
                        + " 创建了接口 " + interfaceName;
        Long projectId = myInterface.getProjectId();
        String type = "create";
        InterfaceLog interfaceLog = new InterfaceLog(projectId, interfaceName, content, user,type);
        interfaceLogService.add(interfaceLog);

    }

    @Override
    public Boolean checkIfExisted(String interfaceName, Long projectId) {
        return interfaceRepository.countAllByNameAndProjectId(interfaceName, projectId) > 0;
    }
}
