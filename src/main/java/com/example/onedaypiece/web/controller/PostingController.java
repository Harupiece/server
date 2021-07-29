package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.PostingService;
import com.example.onedaypiece.web.dto.request.posting.PostingCreateRequestDto;
import com.example.onedaypiece.web.dto.request.posting.PostingUpdateRequestDto;
import com.example.onedaypiece.web.dto.response.posting.PostingResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posting")
public class PostingController {
    private final PostingService postingService;
    /**
     * 1.포스트 저장
     */
    @PostMapping("")
    public ResponseEntity<Long> createPosting(@RequestBody PostingCreateRequestDto postingRequestDto,
                                              @AuthenticationPrincipal UserDetails userDetails){
        log.info("postingRequestDto : {} ",postingRequestDto);
        String email = userDetails.getUsername();
        return ResponseEntity.ok().body(postingService.createPosting(postingRequestDto,email));
    }
    /**
     * 2.포스트 리스트
     */
    @GetMapping("/{page}/{challengeId}")
    public ResponseEntity<List<PostingResponseDto>> getPosting (@PathVariable int page,
                                                                @PathVariable Long challengeId){
        return ResponseEntity.ok().body(postingService.getPosting(page,challengeId));
    }
    /**
     * 3.포스트 업데이트
     */
    @PutMapping("/update/{postingId}")
    public ResponseEntity<Long> updatePosting(@PathVariable Long postingId,
                                              @AuthenticationPrincipal UserDetails userDetails,
                                              @RequestBody PostingUpdateRequestDto postingUpdateRequestDto){
        log.info("postingUpdateRequestDto : {} ",postingUpdateRequestDto);
        String email = userDetails.getUsername();
        return ResponseEntity.ok().body(postingService.updatePosting(postingId,email,postingUpdateRequestDto));
    }
    /**
     * 4.포스트 삭제
     */
    @DeleteMapping("/delete/{postingId}")
    public ResponseEntity<Long> deletePosting(@PathVariable Long postingId,
                                              @AuthenticationPrincipal UserDetails userDetails){
        System.out.println("===================여기옴?============");
        log.info("Username : {} ",userDetails.getUsername());
        String email = userDetails.getUsername();
        return ResponseEntity.ok().body(postingService.deletePosting(postingId,email));
    }

}
