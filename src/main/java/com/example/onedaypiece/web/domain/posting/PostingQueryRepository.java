package com.example.onedaypiece.web.domain.posting;

import com.example.onedaypiece.util.RepositoryHelper;
import com.example.onedaypiece.web.dto.query.posting.PostingListQueryDto;
import com.example.onedaypiece.web.dto.query.posting.QPostingListQueryDto;
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
                .join(posting.challenge)
                .join(posting.member)
                .where(
                        posting.challenge.challengeId.eq(challengeId),
                        posting.postingStatus.isTrue())
                .orderBy(posting.createdAt.desc())
                .offset(page.getOffset())
                .limit(page.getPageSize()+1)
                .fetch();

        return RepositoryHelper.toSlice(postingList,page);
    }
    /**
     * 하루 1개 포스팅
     */
    public boolean existsTodayPosting(LocalDateTime now, Long memberId, Long challengeId) {
        return queryFactory
                .select(posting.postingId)
                .from(posting)
                .where(posting.member.memberId.eq(memberId),
                        posting.challenge.challengeId.eq(challengeId),
                        posting.postingStatus.isTrue(),
                        posting.createdAt.gt(now))
                .fetchFirst() != null;
    }
}