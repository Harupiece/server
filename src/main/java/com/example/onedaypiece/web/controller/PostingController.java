package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.PostingService;
import com.example.onedaypiece.web.dto.request.posting.PostingCreateRequestDto;
import com.example.onedaypiece.web.dto.request.posting.PostingUpdateRequestDto;
import com.example.onedaypiece.web.dto.response.posting.PostingListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public Long createPosting(@RequestBody @Valid PostingCreateRequestDto postingRequestDto,
                                              @AuthenticationPrincipal UserDetails userDetails){
        log.info("createPosting 포스트 저장: {} ",postingRequestDto);
        String email = userDetails.getUsername();
        return postingService.createPosting(postingRequestDto,email);
    }
    /**
     * 2.포스트 리스트
     * @return
     */
    @GetMapping("/{page}/{challengeId}")
    public PostingListDto getPosting (@PathVariable int page,
                                                      @PathVariable Long challengeId){
        log.info("getPosting 전체 포스트 리스트 : {} ",challengeId);
        return postingService.getPosting(page,challengeId);
    }
    /**
     * 3.포스트 업데이트
     */
    @PutMapping("/update/{postingId}")
    public Long updatePosting(@PathVariable Long postingId,
                                              @AuthenticationPrincipal UserDetails userDetails,
                                              @RequestBody PostingUpdateRequestDto postingUpdateRequestDto){
        log.info("updatePosting  포스팅 업데이트 : {} ", postingUpdateRequestDto);
        String email = userDetails.getUsername();
        return postingService.updatePosting(postingId,email, postingUpdateRequestDto);
    }
    /**
     * 4.포스트 삭제
     */
    @DeleteMapping("/delete/{postingId}")
    public Long deletePosting(@PathVariable Long postingId,
                                              @AuthenticationPrincipal UserDetails userDetails){
        log.info("deletePosting 포스트 삭제: {} ", postingId);
        String email = userDetails.getUsername();
        return postingService.deletePosting(postingId,email);
    }

}
