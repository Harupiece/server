package com.example.onedaypiece.web.domain.posting;

import com.example.onedaypiece.util.RepositoryHelper;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.dto.query.posting.PostingListQueryDto;
import com.example.onedaypiece.web.dto.query.posting.SchedulerPostingDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.onedaypiece.web.domain.posting.QPosting.*;


@Repository
@RequiredArgsConstructor
public class PostingQueryRepository  {

    private final JPAQueryFactory queryFactory;

    /**
     * 포스팅 리스트
     */
    public Slice<PostingListQueryDto> findPostingList(Long challengeId, Pageable page){
        QueryResults<PostingListQueryDto> postingQueryResults = queryFactory
                .select(Projections.constructor(PostingListQueryDto.class,
                        posting.postingId,
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
                .where(posting.challenge.challengeId.eq(challengeId).and(posting.postingStatus.isTrue()))
                .join(posting.member).fetchJoin()
                .orderBy(posting.createdAt.desc())
                .offset(page.getOffset())
                .limit(page.getPageSize()+1)
                .fetchResults();

        List<PostingListQueryDto> postingList = postingQueryResults.getResults();

        return RepositoryHelper.toSlice(postingList,page);
    }

    /**
     * 하루 1개 포스팅
     */
    public boolean existsTodayPosting(LocalDateTime now, Member member, Challenge challenge) {
        return queryFactory
                .select(posting.postingId)
                .from(posting)
                .where(posting.member.eq(member)
                        .and(posting.challenge.eq(challenge))
                        .and(posting.postingStatus.isTrue())
                        .and(posting.createdAt.gt(now)))
                .fetchFirst() != null;
    }

//    /**
//     * 벌크 업데이트 쿼리
//     */
//    public long updatePostingStatus(List<Long> postingList) {
//        return queryFactory.update(posting)
//                .set(posting.postingModifyOk,false)
//                .where(posting.postingId.in(postingList))
//                .execute();
//    }

    /**
     * 스케줄러
     */
    public List<Long> findSchedulerUpdatePosting(LocalDateTime today) {

        return queryFactory
                .select(posting.postingId)
                .from(posting)
                .where(posting.postingStatus.isTrue()
                        .and(posting.postingModifyOk.isTrue())
                        .and(posting.createdAt.lt(today)))
                .fetch();
    }

    public List<SchedulerPostingDto> findPostingListTest(List<Long> challengeId, List<Long> memberId, LocalDateTime today){

        return queryFactory.select(
                Projections.constructor(SchedulerPostingDto.class,
                        posting.challenge.challengeId,
                        posting.member.memberId))
                .from(posting)
                .where(posting.postingStatus.isTrue()
                        .and(posting.challenge.challengeId.in(challengeId))
                        .and(posting.member.memberId.in(memberId))
                        .and(posting.createdAt.gt(today)))
                .fetch();
    }

    public List<Long> findAllByChallengeAndPostingApprovalTrue(Challenge challenge){
        return queryFactory.select(posting.member.memberId)
                .from(posting)
                .where(posting.postingApproval.isTrue()
                        .and(posting.challenge.eq(challenge)))
                .fetch();
    }


}
