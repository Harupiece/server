package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.PostingService;
import com.example.onedaypiece.web.dto.request.posting.PostingRequestDto;
import com.example.onedaypiece.web.dto.response.posting.PostingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posting")
public class PostingController {
    private final PostingService postingService;



    /**
     * 1.포스트 저장
     */
    @PostMapping("")
    public ResponseEntity<Long> createPosting(@RequestBody PostingRequestDto postingRequestDto){

        return ResponseEntity.ok().body(postingService.createPosting(postingRequestDto));
    }

    /**
     * 2.포스트 리스트
     */
    @GetMapping("/{page}/{challengeId}")
    public ResponseEntity<List<PostingResponseDto>> getPosting (@PathVariable int page,@PathVariable Long challengeId){


        return ResponseEntity.ok().body(postingService.getPosting(page,challengeId));
    }

    /**
     * 3.포스트 업데이트
     */
    @PutMapping("/update/{postingId}")
    public ResponseEntity<Long> updatePosting(@PathVariable Long postingId, @AuthenticationPrincipal UserDetails userDetails, @RequestBody PostingRequestDto postingRequestDto){
        String email = userDetails.getUsername();
        return ResponseEntity.ok().body(postingService.updatePosting(postingId,email,postingRequestDto));
    }

    /**
     * 4.포스트 삭제
     */
    @PutMapping("/delete/{postingId}")
    public ResponseEntity<Long> deletePosting(@PathVariable Long postingId,@AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();

        return ResponseEntity.ok().body(postingService.deletePosting(postingId,email));
    }




}
