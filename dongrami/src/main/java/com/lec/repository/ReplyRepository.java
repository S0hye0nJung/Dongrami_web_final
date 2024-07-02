package com.lec.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lec.entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {

    @Query("SELECT r FROM Reply r WHERE r.vote.voteId = :voteId ORDER BY r.replyCreate DESC")
    Page<Reply> findByVoteId(@Param("voteId") int voteId, Pageable pageable);

    @Query("SELECT r FROM Reply r WHERE r.member.userId = :userId ORDER BY r.replyCreate DESC")
    List<Reply> findByUserId(@Param("userId") String userId);
}
