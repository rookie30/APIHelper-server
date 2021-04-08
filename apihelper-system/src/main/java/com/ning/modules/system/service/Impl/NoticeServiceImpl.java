package com.ning.modules.system.service.Impl;

import com.ning.modules.system.domain.Notice;
import com.ning.modules.system.repository.NoticeRepository;
import com.ning.modules.system.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    public Integer getUnreadCount(String username) {
        return noticeRepository.findUnreadCount(username);
    }

    @Override
    public List<Notice> getNoticeList(String username) {
        return noticeRepository.findAllByRecipient(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addNotice(String title, String content, String createBy, String recipient) {
        Notice notice = new Notice(title, content, createBy, recipient);
        noticeRepository.save(notice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setReadNotice(List<Notice> readNoticeList) {
        List<Long> noticeIdList = new ArrayList<>();
        for (int i = 0; i < readNoticeList.size(); i++) {
            noticeIdList.add(readNoticeList.get(i).getId());
        }
        List<Notice> noticeList = noticeRepository.findAllById(noticeIdList);
        for(Notice item : noticeList) {
            item.setType(1);
        }
        noticeRepository.saveAll(noticeList);
    }
}
