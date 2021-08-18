package com.example.onedaypiece.web.domain.certification;

import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.posting.Posting;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {

    boolean existsByPostingAndMember(Posting posting, Member member);

}
