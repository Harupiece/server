package com.example.onedaypiece.web.domain.challenge;

import com.example.onedaypiece.util.RepositoryHelper;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.onedaypiece.web.domain.challenge.QChallenge.challenge;
import static com.example.onedaypiece.web.domain.challengeRecord.QChallengeRecord.challengeRecord;

@Repository
@RequiredArgsConstructor
public class ChallengeQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Slice<Challenge> findAllByWords(String words, Pageable page) {
        List<Challenge> challengeList = queryFactory
                .selectFrom(challenge)
                .distinct()
                .join(challengeRecord).on(challenge.challengeId.eq(challengeRecord.challenge.challengeId),
                        challengeRecord.challengeRecordStatus.isTrue())
                .where(
                        challengeRecord.challengeRecordStatus.isTrue(),
                        challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.lt(3L),
                        challenge.categoryName.ne(CategoryName.OFFICIAL),
                        challenge.challengeTitle.contains(words))
                .orderBy(challenge.challengeStartDate.asc())
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
                        challenge.categoryName.eq(CategoryName.OFFICIAL),
                        challenge.challengePassword.eq(""))
                .fetch();
    }

    public Slice<Challenge> findAllBySearch(String categoryName,
                                            int period,
                                            int progress,
                                            Pageable page) {
        List<Challenge> challengeList = queryFactory
                .selectFrom(challenge)
                .distinct()
                .join(challengeRecord).on(challenge.challengeId.eq(challengeRecord.challenge.challengeId),
                        challengeRecord.challengeRecordStatus.isTrue())
                .where(
                        predicateByCategoryNameAndPeriod(categoryName, progress, String.valueOf(period)))
                .orderBy(challenge.challengeProgress.asc(),
                        challenge.challengeStartDate.asc())
                .offset(page.getOffset())
                .limit(page.getPageSize() + 1)
                .fetch();

        return RepositoryHelper.toSlice(challengeList, page);
    }

    private Predicate[] predicateByCategoryNameAndPeriod(String categoryName, int progress, String period) {
        Predicate[] predicates;
        if (progress == 0) {
            if (!categoryName.equals("ALL") && period.equals("0")) { // 카테고리o 기간x
                predicates = new Predicate[]{
                        challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.lt(3L),
                        challenge.categoryName.ne(CategoryName.OFFICIAL),
                        challenge.categoryName.eq(CategoryName.valueOf(categoryName))
                };
            } else if (!categoryName.equals("ALL")) { // 카테고리o, 기간o
                predicates = new Predicate[]{challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.lt(3L),
                        challenge.categoryName.eq(CategoryName.valueOf(categoryName)),
                        challenge.categoryName.ne(CategoryName.OFFICIAL),
                        challenge.tag.eq(getPeriodString(period))
                };
            } else if (period.equals("0")) { // 카테고리x, 기간x
                predicates = new Predicate[]{challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.lt(3L),
                        challenge.categoryName.ne(CategoryName.OFFICIAL)
                };
            } else { // 카테고리x, 기간o
                predicates = new Predicate[]{challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.lt(3L),
                        challenge.categoryName.ne(CategoryName.OFFICIAL),
                        challenge.tag.eq(getPeriodString(period))
                };
            }
        } else if (progress == 1) {
            if (!categoryName.equals("ALL") && period.equals("0")) { // 카테고리o 기간x
                predicates = new Predicate[]{challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.eq(1L),
                        challenge.categoryName.ne(CategoryName.OFFICIAL),
                        challenge.categoryName.eq(CategoryName.valueOf(categoryName))};
            } else if (!categoryName.equals("ALL")) { // 카테고리o, 기간o
                predicates = new Predicate[]{challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.eq(1L),
                        challenge.categoryName.eq(CategoryName.valueOf(categoryName)),
                        challenge.categoryName.ne(CategoryName.OFFICIAL),
                        challenge.tag.eq(getPeriodString(period))};
            } else if (period.equals("0")) { // 카테고리x, 기간x
                predicates = new Predicate[]{challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.eq(1L),
                        challenge.categoryName.ne(CategoryName.OFFICIAL)};
            } else { // 카테고리x, 기간o
                predicates = new Predicate[]{challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.eq(1L),
                        challenge.categoryName.ne(CategoryName.OFFICIAL),
                        challenge.tag.eq(getPeriodString(period))};
            }
        } else {
            if (!categoryName.equals("ALL") && period.equals("0")) { // 카테고리o 기간x
                predicates = new Predicate[]{challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.eq(2L),
                        challenge.categoryName.ne(CategoryName.OFFICIAL),
                        challenge.categoryName.eq(CategoryName.valueOf(categoryName))};
            } else if (!categoryName.equals("ALL")) { // 카테고리o, 기간o
                predicates = new Predicate[]{challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.eq(2L),
                        challenge.categoryName.eq(CategoryName.valueOf(categoryName)),
                        challenge.categoryName.ne(CategoryName.OFFICIAL),
                        challenge.tag.eq(getPeriodString(period))};
            } else if (period.equals("0")) { // 카테고리x, 기간x
                predicates = new Predicate[]{challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.eq(2L),
                        challenge.categoryName.ne(CategoryName.OFFICIAL)};
            } else { // 카테고리x, 기간o
                predicates = new Predicate[]{challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.eq(2L),
                        challenge.categoryName.ne(CategoryName.OFFICIAL),
                        challenge.tag.eq(getPeriodString(period))};
            }
        }
        return predicates;
    }

    private String getPeriodString(String period) {
        return period.equals("4") ? period + "주 이상" : period + "주";
    }

    public Optional<Challenge> findById(Long challengeId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(challenge)
                .join(challenge.member).fetchJoin()
                .where(challenge.challengeId.eq(challengeId))
                .fetchOne());
    }
}