package com.lec.service;

import com.lec.dto.MyReplyDTO;
import com.lec.entity.Reply;
import com.lec.repository.MyReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyReplyService {

    @Autowired
    private MyReplyRepository myReplyRepository;

    public List<MyReplyDTO> getRepliesByUserId(String userId) {
        List<Reply> replies = myReplyRepository.findByMemberUserId(userId);
        return replies.stream().map(reply -> new MyReplyDTO(
                reply.getReplyId(),
                reply.getContent(),
                reply.getVote().getQuestion(),
                reply.getReplyCreate(),
                reply.getVote().getVoteId()
        )).collect(Collectors.toList());
    }
}
