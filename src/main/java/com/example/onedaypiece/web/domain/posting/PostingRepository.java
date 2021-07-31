package com.example.onedaypiece.web.domain.posting;

import com.example.onedaypiece.web.domain.member.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostingRepository extends JpaRepository<Posting,Long> {

    // 스케줄러
    List<Posting> findAllByPostingStatusTrueAndPostingModifyOkTrue();


    // 포스팅 전체 리스트
    @Query("select p from Posting p " +
            "join fetch p.member "+
            "where p.challenge.challengeId = :challengeId ")
    List<Posting> findPostingList(Long challengeId, Pageable pageable);
}
