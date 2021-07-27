package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.CertificationService;
import com.example.onedaypiece.web.domain.certification.Certification;
import com.example.onedaypiece.web.dto.request.certification.CertificationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/posting")
@RestController
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping("")
    public ResponseEntity<Long> createCertification(CertificationRequestDto certificationRequestDto){

        return ResponseEntity.ok().body(certificationService.createCertification(certificationRequestDto));

    }
}
