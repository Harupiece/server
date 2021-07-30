package com.example.onedaypiece.web.domain.certification;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.posting.Posting;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {

    boolean existsByPostingAndMember(Posting posting, Member member);


    @Query("select c  " +
            "from Certification c " +
            "where c.posting.challenge.challengeId = :challengeId")
    List<Certification> findAllByPosting(Long challengeId);


    @Query("select c " +
            "from Certification c " +
            "join fetch c.posting " +
            "where c.posting.challenge.challengeId = :challengeId")
    List<Certification> findChallenge (Long challengeId, Pageable pageable);


    @Query("select c from Certification c left outer join c.posting where c.posting.postingId in :postingId")
    List<Certification> findTest(List<Long> postingId);
}
