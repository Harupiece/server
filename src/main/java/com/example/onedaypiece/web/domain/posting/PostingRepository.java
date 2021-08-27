package com.example.onedaypiece.web.domain.posting;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface PostingRepository extends JpaRepository<Posting,Long> {

    @Modifying(clearAutomatically = true)
    @Query("update Posting p set p.postingModifyOk = false where p.postingId in :postingIdList")
    int updatePostingStatus(List<Long> postingIdList);

    @Modifying(clearAutomatically = true)
    @Query("update Posting p set p.postingApproval = true  where p in :updatePostingId")
    int updatePostingApproval (List<Posting> updatePostingId );
}
