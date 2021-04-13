package com.ning.modules.system.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.javafaker.Faker;
import com.ning.modules.system.domain.InterfaceLog;
import com.ning.modules.system.domain.MyInterface;
import com.ning.modules.system.repository.InterfaceRepository;
import com.ning.modules.system.service.InterfaceLogService;
import com.ning.modules.system.service.InterfaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class InterfaceServiceImpl implements InterfaceService {

    private static final Integer testCount = 10;

    private final InterfaceRepository interfaceRepository;
    private final DateFormat dateFormat = DateFormat.getDateTimeInstance();
    private final InterfaceLogService interfaceLogService;
    private Faker faker = new Faker();

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

    @Override
    public Mono<String> manualTest(MyInterface myInterface) {
        String requestType = myInterface.getRequestType();
        if(("GET").equals(requestType)) {
            return this.getMethod(myInterface);
        } else {
            return this.postMethod(myInterface);
        }
    }

    @Override
    public List<Map<String, Object>> autoTest(MyInterface myInterface) {
        String requestType = myInterface.getRequestType();
        if(("GET").equals(requestType)) {
            return this.autoGetMethod(myInterface);
        } else {
            return this.autoPostMethod(myInterface);
        }
    }

    /**
     * 手动测试GET方法
     * @param myInterface
     * @return
     */
    private Mono<String> getMethod(MyInterface myInterface) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        List<Map<String, String>> interfaceParams = JSONArray.parseObject(myInterface.getParams(), List.class);
        if(interfaceParams.size() != 0) {
            for (int i = 0; i < interfaceParams.size(); i++) {
                params.add(interfaceParams.get(i).get("key"), interfaceParams.get(i).get("value"));
            }
        }
        WebClient client = WebClient.create(myInterface.getRequestUrl());
        String uri = UriComponentsBuilder.fromUriString("")
                .queryParams(params)
                .toUriString();
        return client
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(WebClientResponseException.class, err -> {
                    throw new RuntimeException(err.getResponseBodyAsString());
                });
    }

    /**
     * 手动测试POST方法
     * @param myInterface
     * @return
     */
    private Mono<String> postMethod(MyInterface myInterface) {
        Map<String, Object> interfaceBody = (Map<String, Object>) JSON.parse(myInterface.getBody());
        List<Map<String, String>> bodyParams = (List<Map<String, String>>) interfaceBody.get("content");
        Map<String, String> body = new HashMap<>();
        if(bodyParams.size() != 0) {
            for (int i = 0; i < bodyParams.size(); i++) {
                body.put(bodyParams.get(i).get("key"), bodyParams.get(i).get("value"));
            }
        }
        return WebClient.create(myInterface.getRequestUrl())
                .post()
                .syncBody(body)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(WebClientResponseException.class, err -> {
                    throw new RuntimeException(err.getResponseBodyAsString());
                });
    }

    /**
     * 自动测试-GET
     * @param myInterface
     * @return
     */
    private List<Map<String, Object>> autoGetMethod(MyInterface myInterface) {
        List<Map<String, Object>> res = new ArrayList<>();
        List<String> keyList = new ArrayList<>(); // key数组
        List<Map<String, String>> interfaceParams = JSONArray.parseObject(myInterface.getParams(), List.class);
        // 将key提取出来，存入key数组中
        if(interfaceParams.size() != 0) {
            for (int i = 0; i < interfaceParams.size(); i++) {
                keyList.add(interfaceParams.get(i).get("key"));
            }
        }
        for (int i = 0; i < testCount; i++) {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            Map<String, String> paramsMap = new HashMap<>(); // 参数map
            // 遍历key数组，根据key值fake数据
            for (int j = 0; j < keyList.size(); j++) {
                String key = keyList.get(j);
                String param = fakeParams(key);
                params.add(key, param);
                paramsMap.put(key, param);
            }
            // 发送请求
            WebClient client = WebClient.create(myInterface.getRequestUrl());
            String uri = UriComponentsBuilder.fromUriString("")
                    .queryParams(params)
                    .toUriString();
            Mono<String> result = client
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnError(WebClientResponseException.class, err -> {
                        throw new RuntimeException(err.getResponseBodyAsString());
                    });
            // 记录结果
            Map<String, Object> resultMap = new HashMap<>(); // 测试结果map
            resultMap.put("params", paramsMap);
            resultMap.put("index", i + 1);
            try {
                String blockResult = result.block();
                resultMap.put("result", blockResult);
            }catch (RuntimeException err) {
                resultMap.put("result", err.getMessage());
            }finally {
                res.add(resultMap);
            }
        }
        return res;
    }

    /**
     * 自动测试-POST
     * @param myInterface
     * @return
     */
    private List<Map<String, Object>> autoPostMethod(MyInterface myInterface) {
        List<Map<String, Object>> res = new ArrayList<>();
        List<String> keyList = new ArrayList<>(); // key数组
        List<Map<String, String>> interfaceBody = (List<Map<String, String>>) JSON.parseObject(myInterface.getBody()).get("content");
        // 将key提取出来
        if(interfaceBody.size() != 0) {
            for (int i = 0; i < interfaceBody.size(); i++) {
                keyList.add(interfaceBody.get(i).get("key"));
            }
        }
        for (int i = 0; i < testCount; i++) {
            MultiValueMap<String, String> bodys = new LinkedMultiValueMap<>();
            Map<String, String> paramsMap = new HashMap<>(); // 参数map
            // 遍历key数组，根据key值fake数据
            for (int j = 0; j < keyList.size(); j++) {
                String key = keyList.get(j);
                String param = fakeParams(key);
                bodys.add(key, param);
                paramsMap.put(key, param);
            }
            // 发送请求
            WebClient client = WebClient.create(myInterface.getRequestUrl());
            Mono<String> result = client
                    .post()
                    .syncBody(bodys)
                    .retrieve()
                    .bodyToMono(String.class).doOnError(WebClientResponseException.class, err -> {
                        throw new RuntimeException(err.getResponseBodyAsString());
                    });
            // 记录结果
            Map<String, Object> resultMap = new HashMap<>(); // 测试结果map
            resultMap.put("params", paramsMap);
            resultMap.put("index", i + 1);
            try {
                String blockResult = result.block();
                resultMap.put("result", blockResult);
            }catch (RuntimeException err) {
                resultMap.put("result", err.getMessage());
            }finally {
                res.add(resultMap);
            }
        }
        return res;
    }

    /**
     * fake参数
     * @param key
     * @return
     */
    private String fakeParams(String key) {
        // 判断key是否为密码类型
        if(Pattern.matches(".*(password|pwd).*", key)) {
            return faker.internet().password();
        }
        // 账号类型
        if(Pattern.matches(".*(username|account).*", key)) {
            int len = faker.number().numberBetween(5, 20);
            return faker.number().digits(len);
        }
        // 城市名称类型
        if(Pattern.matches(".*(city).*", key)) {
            return faker.address().cityName();
        }
        // 图片类型
        if(Pattern.matches(".*(avatar|img|image|picture).*", key)) {
            return faker.internet().avatar();
        }
        // 日期型
        if(Pattern.matches(".*(date|time|day).*", key)) {
            return String.valueOf(faker.date().past(10, TimeUnit.DAYS));
        }
        // 电话号码型
        if(Pattern.matches(".*(phone).*", key)) {
            return faker.phoneNumber().phoneNumber();
        }
        return null;
    }


}
