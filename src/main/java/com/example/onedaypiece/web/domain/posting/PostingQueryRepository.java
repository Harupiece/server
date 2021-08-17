package com.example.onedaypiece.web.domain.posting;

import com.example.onedaypiece.util.RepositoryHelper;
import com.example.onedaypiece.web.dto.query.posting.PostingListQueryDto;
import com.example.onedaypiece.web.dto.query.posting.QPostingListQueryDto;
import com.example.onedaypiece.web.dto.query.posting.QSchedulerIdListDto;
import com.example.onedaypiece.web.dto.query.posting.SchedulerIdListDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.onedaypiece.web.domain.posting.QPosting.posting;


@Repository
@RequiredArgsConstructor
public class PostingQueryRepository  {

    private final JPAQueryFactory queryFactory;

    /**
     * 포스팅 리스트
     */
    public Slice<PostingListQueryDto> findPostingList(Long challengeId, Pageable page){
        List<PostingListQueryDto> postingList = queryFactory
                .select(new QPostingListQueryDto(
                        posting.postingId,
                        posting.member.memberId,
                        posting.member.nickname,
                        posting.member.profileImg,
                        posting.postingImg,
                        posting.postingContent,
                        posting.postingApproval,
                        posting.postingModifyOk,
                        posting.createdAt,
                        posting.modifiedAt,
                        posting.postingCount
                ))
                .from(posting)
                .where(
                        eqChallengeId(challengeId),
                        postingStatusIsTrue())
                .leftJoin(posting.challenge)
                .orderBy(posting.createdAt.desc())
                .offset(page.getOffset())
                .limit(page.getPageSize()+1)
                .fetch();

        return RepositoryHelper.toSlice(postingList,page);
    }



    /**
     * 하루 1개 포스팅
     */
    public boolean existsTodayPosting(LocalDateTime now, Long member, Long challenge) {
        return queryFactory
                .select(posting.postingId)
                .from(posting)
                .where(posting.member.memberId.eq(member),
                        eqChallengeId(challenge),
                        postingStatusIsTrue(),
                        posting.createdAt.gt(now))
                .fetchFirst() != null;
    }

    /**
     * 스케줄러
     */





    private BooleanExpression eqChallengeId(Long challengeId) {
        return challengeId != null ? posting.challenge.challengeId.eq(challengeId) : null;
    }

    private BooleanExpression postingStatusIsTrue() {
        return posting.postingStatus.isTrue();
    }


}