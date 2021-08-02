package com.example.onedaypiece.service;

import com.example.onedaypiece.web.domain.history.UserHistory;
import com.example.onedaypiece.web.domain.history.UserHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserHistoryService {

    private final UserHistoryRepository userHistoryRepository;

    public List<UserHistory> getUserHistory(Long memberId) {
        return userHistoryRepository.findAllByMemberId(memberId);
    }

    @Transactional
    public void putUserHistoryStatusFalse(Long memberId) {
        userHistoryRepository.findAllByStatusFalseAndMemberId(memberId).forEach(UserHistory::setCheckStatusTrue);
    }
}
