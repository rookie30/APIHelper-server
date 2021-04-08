package com.ning.modules.system.service.Impl;

import com.ning.modules.system.domain.InterfaceLog;
import com.ning.modules.system.domain.MyInterface;
import com.ning.modules.system.repository.InterfaceRepository;
import com.ning.modules.system.service.InterfaceLogService;
import com.ning.modules.system.service.InterfaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.util.ArrayList;
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
    @Transactional(rollbackFor = Exception.class)
    public void create(MyInterface myInterface) {
        Date createTime = new Date();
        // 记录日志
        String user = myInterface.getCreateBy();
        String interfaceName = myInterface.getName();
        String content = user + " 于 "+ dateFormat.format(createTime)
                        + " 创建了接口 " + interfaceName;
        Long projectId = myInterface.getProjectId();
        String type = "create";
        InterfaceLog interfaceLog = new InterfaceLog(projectId, myInterface, content, user,type);
        interfaceLogService.add(interfaceLog);
        if(myInterface.getLogs() == null) {
            myInterface.setLogs(new ArrayList<>());
        }
        myInterface.getLogs().add(interfaceLog);
        interfaceRepository.save(myInterface);
    }

    @Override
    public Boolean checkIfExisted(String interfaceName, Long projectId) {
        return interfaceRepository.countAllByNameAndProjectId(interfaceName, projectId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MyInterface newInterface) {
        Long projectId = newInterface.getProjectId();
        String createBy = newInterface.getCreateBy();
        Date createTime = newInterface.getCreateTime();
        MyInterface oldInterface = interfaceRepository.findByProjectIdAndCreateByAndCreateTime(projectId, createBy, createTime);
        if(oldInterface == null) {
            throw new Error("接口不存在");
        }
        oldInterface.setName(newInterface.getName());
        oldInterface.setUpdateBy(newInterface.getUpdateBy());
        oldInterface.setIntroduce(newInterface.getIntroduce());
        oldInterface.setBody(newInterface.getBody());
        oldInterface.setParams(newInterface.getParams());
        oldInterface.setHeaders(newInterface.getHeaders());
        oldInterface.setRequestType(newInterface.getRequestType());
        oldInterface.setRequestUrl(newInterface.getRequestUrl());
        Date updateTime = new Date();
        // 记录日志
        String content = oldInterface.getUpdateBy() + " 于 " +
                dateFormat.format(updateTime) + " 更新了此接口 ";
        String logType = "update";
        InterfaceLog interfaceLog = new InterfaceLog(projectId, oldInterface, content, newInterface.getUpdateBy(), logType);
        oldInterface.getLogs().add(interfaceLog);
        interfaceLogService.add(interfaceLog);
        interfaceRepository.save(oldInterface);
    }
}
