package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.UserHistoryService;
import com.example.onedaypiece.web.domain.history.UserHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserHistoryController {

    private final UserHistoryService userHistoryService;

    @GetMapping("/api/member/user-history/{memberId}")
    public ResponseEntity<List<UserHistory>> getUserHistory(@PathVariable Long memberId) {
        return ResponseEntity.ok().body(userHistoryService.getUserHistory(memberId));
    }

    @PostMapping("/api/member/user-history/{memberId}")
    public ResponseEntity<Void> putUserHistoryStatusFalse(@PathVariable Long memberId) {
        userHistoryService.putUserHistoryStatusFalse(memberId);
        return ResponseEntity.ok().build();
    }
}
