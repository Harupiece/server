package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.CertificationService;
import com.example.onedaypiece.web.domain.certification.Certification;
import com.example.onedaypiece.web.dto.request.certification.CertificationRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/certification")
@RestController
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping("")
    public ResponseEntity<Long> createCertification(
            @RequestBody CertificationRequestDto certificationRequestDto,
            @AuthenticationPrincipal UserDetails userDetails){

        log.info("certificationRequestDto : {} ",certificationRequestDto);
        return ResponseEntity.ok().body(certificationService.createCertification(certificationRequestDto,userDetails));

    }
}
