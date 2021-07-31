package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.AdminService;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/api/admin/challenge")
    public ResponseEntity<List<Challenge>> getAllChallengeByAdmin() {
        return ResponseEntity.ok().body(adminService.getAllChallengeByAdmin());
    }

    @DeleteMapping("/api/admin/challenge/{challengeId}") // admin 권한으로 DB에서 아예 삭제
    public ResponseEntity<Void> deleteChallengeByAdmin(@PathVariable Long challengeId) {
        adminService.deleteChallengeByAdmin(challengeId);
        return ResponseEntity.ok().build();
    }
}
