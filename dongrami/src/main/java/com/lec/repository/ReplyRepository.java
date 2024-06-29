package com.lec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lec.entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    
    @Query("SELECT r FROM Reply r WHERE r.vote.voteId = :voteId")
    List<Reply> findByVoteId(@Param("voteId") int voteId);
}
