package com.example.onedaypiece.web.domain.posting;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostingQueryRepository {

    private final EntityManager em;

    public List<Posting> findPostingList(Long challengeId, Pageable pageable){
        return em.createQuery("select p " +
                "from Posting p " +
                "left join fetch p.member " +
                "where p.challenge.challengeId = :challengeId ",Posting.class)
                .setParameter("challengeId",challengeId)
                .getResultList();
    }


}
