package com.example.onedaypiece.web.domain.posting;

import com.example.onedaypiece.web.domain.member.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostingRepository extends JpaRepository<Posting,Long> {



    // 포스팅 전체 리스트
    @Query("select p from Posting p " +
            "join fetch p.member "+
            "where p.challenge.challengeId = :challengeId ")
    List<Posting> findPostingList(Long challengeId, Pageable pageable);


    // 스케줄러
    @Query("select p from Posting p " +
            "where p.postingStatus=true " +
            "and p.postingModifyOk=true " +
            "and p.createdAt < :today")
    List<Posting> findAllByPostingStatusTrueAndPostingModifyOkTrue(LocalDateTime today);

    @Modifying(clearAutomatically = true)
    @Query("update Posting p set p.postingModifyOk = false where p in :postingList")
    int updatePostingStatus(List<Posting> postingList);

    List<Posting> findAllByPostingStatusTrueAndPostingModifyOkFalse();
}
