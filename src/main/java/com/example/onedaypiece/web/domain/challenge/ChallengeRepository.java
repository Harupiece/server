package com.example.onedaypiece.web.domain.challenge;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {


    Optional<Challenge> findByChallengeStatusTrueAndChallengeId(Long challengeId);

    List<Challenge> findAllByChallengeStatusTrueAndChallengeProgressLessThan(Long challengeProgress);

//    @Query("select c from Challenge c " +
//            "Where c.challengeStatus = true and c.challengeProgress = 1 and c.categoryName = :categoryName " +
//            "ORDER BY c.modifiedAt DESC")
//    List<Challenge> findAllByCategoryNameOrderByModifiedAtDescListed(CategoryName categoryName, Pageable pageable);

//    @Query("select c from Challenge c " +
//            "WHERE c.challengeStatus = true and c.challengeProgress = 1 and c.challengeTitle like %?1%" +
//            "ORDER BY c.modifiedAt DESC")
//    List<Challenge> findAllByWords(String words, Pageable pageable);

//    @Modifying(clearAutomatically = true)
//    @Query("update Challenge c set c.challengeProgress = :progress where c in :challengeList")
//    int updateChallengeProgress(Long progress, List<Challenge> challengeList);
}