package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AdminController {

    private final AdminService adminService;

    @DeleteMapping("/api/admin/challenge/{challengeId}") // admin 권한으로 DB에서 아예 삭제
    public ResponseEntity<Void> deleteChallengeByAdmin(@PathVariable Long challengeId) {
        adminService.deleteChallengeByAdmin(challengeId);
        return ResponseEntity.ok().build();
    }
}
