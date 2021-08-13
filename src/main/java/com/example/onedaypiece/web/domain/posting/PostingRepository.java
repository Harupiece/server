package com.example.onedaypiece.web.domain.posting;


import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostingRepository extends JpaRepository<Posting,Long> {

    @Modifying(clearAutomatically = true)
    @Query("update Posting p set p.postingModifyOk = false where p.postingId in :postingIdList")
    int updatePostingStatus(List<Long> postingIdList);

    @Query("select p.challenge.challengeId from Posting p " +
            "Where p.challenge.challengeStatus = true and p.challenge = :challenge and p.member = :member")
    List<Long> findAllByChallengeAndMember(Challenge challenge, Member member);
}
