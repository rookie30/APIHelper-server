package com.ning.modules.system.controller;

import com.ning.modules.security.TokenProvider;
import com.ning.modules.system.domain.Notice;
import com.ning.modules.system.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
@Api(tags = "通知管理模块")
public class NoticeController {

    private final TokenProvider tokenProvider;
    private final NoticeService noticeService;

    @GetMapping("/getUnreadCount")
    @ApiOperation("获取未读通知数量")
    public ResponseEntity<Object> getUnreadCount(HttpServletRequest request) {
//        String username = tokenProvider.getUsername(tokenProvider.getToken(request));
        String username = this.getUsername(request);
        Integer count = noticeService.getUnreadCount(username);
        Map<String, Object> res = new HashMap<String, Object>(2){{
            put("status", "200");
            put("count", count);
        }};
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/getNoticeList")
    @ApiOperation(value = "获取所有通知")
    public ResponseEntity<Object> getNoticeList(HttpServletRequest request) {
        String username = this.getUsername(request);
        List<Notice> noticeList = noticeService.getNoticeList(username);
        Map<String, Object> res = new HashMap<String, Object>(2){{
            put("status", "200");
            put("noticeList", noticeList);
        }};
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/setReadNotice")
    @ApiOperation(value = "将消息标记为已读")
    public ResponseEntity<Object> setReadNotice(@RequestBody List<Notice> noticeList) {
        noticeService.setReadNotice(noticeList);
        Map<String, Object> res = new HashMap<String, Object>(1){{
            put("status", "200");
        }};
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    private String getUsername(HttpServletRequest request) {
        return tokenProvider.getUsername(tokenProvider.getToken(request));
    }
}
