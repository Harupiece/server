package com.example.onedaypiece.web.domain.challenge;

import com.example.onedaypiece.util.RepositoryHelper;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.onedaypiece.web.domain.challenge.QChallenge.challenge;
import static com.example.onedaypiece.web.domain.challengeRecord.QChallengeRecord.challengeRecord;

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
    public List<Challenge> findAllByCategoryName(CategoryName categoryName, Pageable page) {
//        List<Challenge> challengeList =
        return queryFactory
                .selectFrom(challenge)
                .where(challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.eq(1L),
                        challenge.categoryName.eq(categoryName))
                .orderBy(challenge.modifiedAt.desc())
                .offset(page.getOffset())
                .limit(page.getPageSize() + 1)
                .fetch();

//        return RepositoryHelper.toSlice(challengeList, page);
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
                .where(challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.eq(1L),
                        challenge.categoryName.ne(CategoryName.OFFICIAL),
                        challenge.challengeTitle.contains(words))
                .orderBy(challenge.modifiedAt.asc())
                .offset(page.getOffset())
                .limit(page.getPageSize() + 1)
                .fetch();

        return RepositoryHelper.toSlice(challengeList, page);
    }

    public List<Challenge> findAllByOfficialChallenge() {
        return queryFactory
                .selectFrom(challenge)
                .where(challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.eq(1L),
                        challenge.categoryName.eq(CategoryName.OFFICIAL))
                .fetch();
    }

    public Slice<Challenge> findAllByCategoryNameAndPeriod(String categoryName, int period, Pageable page) {
        List<Challenge> challengeList = queryFactory
                .selectFrom(challenge)
                .where(predicateByCategoryNameAndPeriod(categoryName, period))
                .orderBy(challengeRecord.challenge.modifiedAt.desc())
                .offset(page.getOffset())
                .limit(page.getPageSize() + 1)
                .fetch();

        return RepositoryHelper.toSlice(challengeList, page);
    }

    private Predicate[] predicateByCategoryNameAndPeriod(String categoryName, int period) {
        Predicate[] predicates;
        if (!categoryName.equals("N") && period == 0) { // 카테고리o 기간x
            predicates = new Predicate[]{challenge.challengeStatus.isTrue(),
                    challenge.challengeProgress.eq(1L),
                    challenge.categoryName.ne(CategoryName.OFFICIAL),
                    challengeRecord.challenge.categoryName.eq(CategoryName.valueOf(categoryName))};
        } else if (!categoryName.equals("N")) { // 카테고리o, 기간o
            predicates = new Predicate[]{challenge.challengeStatus.isTrue(),
                    challenge.challengeProgress.eq(1L),
                    challengeRecord.challenge.categoryName.eq(CategoryName.valueOf(categoryName)),
                    challenge.categoryName.ne(CategoryName.OFFICIAL),
                    challengeRecord.challenge.tag.eq(period + "주")};
        } else if (period == 0) { // 카테고리x, 기간x
            predicates = new Predicate[]{challenge.challengeStatus.isTrue(),
                    challenge.challengeProgress.eq(1L),
                    challenge.categoryName.ne(CategoryName.OFFICIAL)};
        } else { // 카테고리x, 기간o
            predicates = new Predicate[]{challenge.challengeStatus.isTrue(),
                    challenge.challengeProgress.eq(1L),
                    challenge.categoryName.ne(CategoryName.OFFICIAL),
                    challengeRecord.challenge.tag.eq(period + "주")};
        }
        return predicates;
    }
}