package com.example.onedaypiece.web.domain.challenge;

import com.example.onedaypiece.util.RepositoryHelper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.onedaypiece.web.domain.challenge.QChallenge.challenge;

@Repository
@RequiredArgsConstructor
public class ChallengeQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * @Query("select c from Challenge c " +
     * "Where c.challengeStatus = true and c.challengeProgress = 1 and c.categoryName = :categoryName " +
     * "ORDER BY c.modifiedAt DESC")
     * List<Challenge> findAllByCategoryNameOrderByModifiedAtDescListed(CategoryName categoryName, Pageable pageable);
     **/
    public Slice<Challenge> findAllByCategoryName(CategoryName categoryName, Pageable page) {
        List<Challenge> challengeList = queryFactory
                .selectFrom(challenge)
                .where(challenge.challengeStatus.eq(true),
                        challenge.challengeProgress.eq(1L),
                        challenge.categoryName.eq(categoryName))
                .orderBy(challenge.modifiedAt.desc())
                .offset(page.getOffset())
                .limit(page.getPageSize() + 1)
                .fetch();

        return RepositoryHelper.toSlice(challengeList, page);
    }

    /**
     * @Query("select c from Challenge c " +
     * "WHERE c.challengeStatus = true and c.challengeProgress = 1 and c.challengeTitle like %?1%" +
     * "ORDER BY c.modifiedAt DESC")
     * List<Challenge> findAllByWordsAndChallengeStatusTrueOrderByModifiedAtDesc(String words, Pageable pageable);
     **/
    public Slice<Challenge> findAllByWords(String words, Pageable page) {
        List<Challenge> challengeList = queryFactory
                .selectFrom(challenge)
                .where(challenge.challengeStatus.eq(true),
                        challenge.challengeProgress.eq(1L),
                        challenge.challengeTitle.like(words))
                .orderBy(challenge.modifiedAt.desc())
                .offset(page.getOffset())
                .limit(page.getPageSize() + 1)
                .fetch();

        return RepositoryHelper.toSlice(challengeList, page);
    }

    /**
     * @Modifying(clearAutomatically = true)
     * @Query("update Challenge c set c.challengeProgress = :progress where c in :challengeList")
     * int updateChallengeProgress(Long progress, List<Challenge> challengeList);
     **/
    public Long updateChallengeProgress(Long progress, List<Challenge> challengeList) {
        return queryFactory
                .update(challenge)
                .set(challenge.challengeProgress, progress)
                .where(challenge.in(challengeList))
                .execute();
    }
}