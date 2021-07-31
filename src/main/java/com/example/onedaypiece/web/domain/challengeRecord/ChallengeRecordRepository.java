package com.example.onedaypiece.web.domain.challengeRecord;

import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.member.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChallengeRecordRepository extends JpaRepository<ChallengeRecord, Long> {

    @Query("select c from ChallengeRecord c join fetch c.challenge " +
            "where c.challengeRecordStatus = true " +
            "and c.challenge.challengeStatus = true " +
            "and c.challenge.challengeProgress < 3")
    List<ChallengeRecord> findAllByChallengeStatusTrueAndChallengeProgressLessThan();

    @Query("select c from ChallengeRecord c left join fetch c.challenge " +
            "where c.challengeRecordStatus = true " +
            "and c.challenge.challengeStatus = true " +
            "and c.challenge.challengeProgress = 1 " +
            "order by c.modifiedAt desc")
    List<ChallengeRecord> findAllByChallengeRecordByCategoryName(CategoryName categoryName, Pageable pageable);

//    @Modifying
//    @Query("delete from ChallengeRecord c " +
//            "Where c.challenge.challengeStatus = true " +
//            "and c.challenge.challengeId = :challengeId " +
//            "and c.member = :member")
//    void deleteByChallengeIdAndMember(Long challengeId, Member member);

    void deleteByChallengeAndMember(Challenge challenge, Member member);

    @Query("select c from ChallengeRecord c Where c.challengeRecordStatus = true and c.challenge = :challenge")
    List<ChallengeRecord> findAllByChallenge(Challenge challenge);

    @Query("select c from ChallengeRecord c Where c.challengeRecordStatus = true " +
            "and c.challenge.challengeId = :challengeId")
    List<ChallengeRecord> findAllByChallengeId(Long challengeId);

    @Query("select c from ChallengeRecord c left join fetch c.challenge " +
            "Where c.challengeRecordStatus = true and c.challenge in :challenge")
    List<ChallengeRecord> findAllByChallengeList(List<Challenge> challenge);

    @Query("select c " +
            "from ChallengeRecord c left join fetch c.challenge " +
            "where c.challenge.challengeStatus = true and c.challenge.challengeProgress = 1 " +
            "and c.member.email not in :email group by c.challenge.challengeId " +
            "order by count(c.challenge.challengeId) desc")
    List<ChallengeRecord> findAllStatusTrueAndProgressNotStartedYet(String email, Pageable pageable);

    void deleteAllByChallenge(Challenge challenge);

    @Query("select c from ChallengeRecord c join fetch c.challenge " +
            "Where c.challengeRecordStatus = true and c.member.email = :email")
    List<ChallengeRecord> findAllByMemberEmail(String email);

    @Query("select CASE WHEN count(c)>0 then true else false end " +
            "from ChallengeRecord c " +
            "Where c.challengeRecordStatus = true and c.member = :member and c.challenge = :challenge")
    boolean existsByChallengeAndMember(Challenge challenge, Member member);

    @Query("select count(c) from ChallengeRecord c Where c.challengeRecordStatus = true and c.challenge = :challenge")
    int countByChallenge(Challenge challenge);


////     챌린지에 참여한인원원
//   @Query("select count(c) from ChallengeRecord c Where c.challenge = :challenge")
//    int challengecount(Challenge challenge);



   // 진행중인첼린지
   @Query("select c from ChallengeRecord c Where c.challengeRecordStatus = true and c.member = :member and c.challenge.challengeProgress = :progress")
   List<ChallengeRecord> findAllByMemberAndProgress(Member member, Long progress);

}
