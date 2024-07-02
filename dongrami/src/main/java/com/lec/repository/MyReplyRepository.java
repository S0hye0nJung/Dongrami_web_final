package com.lec.repository;

import com.lec.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyReplyRepository extends JpaRepository<Reply, Integer> {
    List<Reply> findByMemberUserId(String userId);
}
