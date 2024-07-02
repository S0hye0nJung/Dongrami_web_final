package com.lec.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import com.lec.entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    

	/*
	 * @Query("SELECT r FROM Reply r WHERE r.vote.voteId = :voteId ORDER BY r.replyCreate DESC"
	 * ) List<Reply> findByVoteId(@Param("voteId") int voteId);
	 */
	@Query("SELECT r FROM Reply r WHERE r.vote.voteId = :voteId ORDER BY r.replyCreate DESC")
    Page<Reply> findByVoteId(@Param("voteId") int voteId, Pageable pageable);


}
