package com.lec.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lec.entity.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {
}
