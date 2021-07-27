package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.PostingService;
import com.example.onedaypiece.web.dto.request.PostingRequestDto;
import com.example.onedaypiece.web.dto.response.posting.PostingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PostingController {
    private final PostingService postingService;



    @GetMapping("/api/member/posting")
    public ResponseEntity<Long> createPosting(@RequestBody PostingRequestDto postingRequestDto){

        return ResponseEntity.ok().body(postingService.createPosting(postingRequestDto));
    }




}
