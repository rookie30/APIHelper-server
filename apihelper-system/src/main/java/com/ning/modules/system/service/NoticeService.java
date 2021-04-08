package com.ning.modules.system.service;

import com.ning.modules.system.domain.Notice;

import java.util.List;

public interface NoticeService {

    Integer getUnreadCount(String username);

    List<Notice> getNoticeList(String username);

    void addNotice(String title, String content, String createBy, String recipient);

    void setReadNotice(List<Notice> readNoticeList);
}
