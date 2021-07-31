package com.example.onedaypiece.service;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.domain.posting.Posting;
import com.example.onedaypiece.web.domain.posting.PostingRepository;
import com.example.onedaypiece.web.dto.request.challenge.ChallengeRequestDto;
import com.example.onedaypiece.web.dto.request.posting.PostingCreateRequestDto;
import com.example.onedaypiece.web.dto.request.posting.PostingUpdateRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
public class PostingServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ChallengeRepository challengeRepository;
    @Autowired
    PostingRepository postingRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void createPosting() {

        //given
        PostingCreateRequestDto postingRequestDto = getPostingRequestDto();
        Member member = getMember();
        Challenge challenge = getChallenge(member);

        Posting posting = Posting.createPosting(postingRequestDto, member, challenge);

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(challenge.getChallengeStartDate())) {
            throw new ApiRequestException("챌린지 시작 후에 게시글 등록 가능합니다.");
        }

        //when
        postingRepository.save(posting);

        Posting findPosting =
                postingRepository.findById(posting.getPostingId()).orElseThrow(() -> new ApiRequestException("포스팅이 없습니다."));

        //then
        assertEquals(posting.getPostingId(), findPosting.getPostingId());

    }
    @Test
    public void updatePosting() {

        //given

        Member member = getMember();
        Challenge challenge =getChallenge(member);
        PostingCreateRequestDto postingRequestDto = getPostingRequestDto();
        Posting posting = Posting.createPosting(postingRequestDto, member, challenge);

        PostingUpdateRequestDto postingUpdateRequestDto = new PostingUpdateRequestDto("이미지입니다.", "테스트 콘텐츠 입니다");

        postingRepository.save(posting);


        //when
        posting.updatePosting(postingUpdateRequestDto);

        //then

        assertEquals(posting.getPostingContent(), postingUpdateRequestDto.getPostingContent());
        assertEquals(posting.getPostingImg(), postingUpdateRequestDto.getPostingImg());

    }
    @Test
    public void deletePosting() {
        //given
        Member member = getMember();
        Challenge challenge = getChallenge(member);
        PostingCreateRequestDto postingRequestDto = getPostingRequestDto();
        Posting posting = Posting.createPosting(postingRequestDto, member, challenge);

        //when

        posting.deletePosting();

        //then

        assertFalse(posting.isPostingStatus());
    }

    private PostingCreateRequestDto getPostingRequestDto() {
        return new PostingCreateRequestDto("이미지", "컨텐츠", 12L, 1L);
    }
    private Member getMember() {
        return new Member("test1@naver.com", passwordEncoder.encode("1234"), "닉네임1", "프로필1");
    }
    private Challenge getChallenge(Member member) {
        CategoryName categoryName = null;
        ChallengeRequestDto challengeTestRequestDto = new ChallengeRequestDto(
                "challengeTitle",
                "challengeContent",
                "", categoryName,
                LocalDateTime.now(),
                LocalDateTime.now().
                        plusDays(1),
                "challengeImgUrl",
                "challengeGood",
                "challengeBad",
                "challengeHoliday");

        return new Challenge(challengeTestRequestDto,member);
    }

}