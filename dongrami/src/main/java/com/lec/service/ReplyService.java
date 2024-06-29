package com.lec.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lec.entity.Reply;
import com.lec.entity.Vote;
import com.lec.repository.ReplyRepository;
import com.lec.repository.VoteRepository;

@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;
    
    @Autowired
    private VoteRepository voteRepository;

    public List<Reply> getAllReplies() {
        return replyRepository.findAll();
    }

    public Reply getReplyById(int id) {
        return replyRepository.findById(id).orElse(null);
    }

    public List<Reply> getRepliesByVoteId(int voteId) {
        return replyRepository.findByVoteId(voteId);
    }

    public Reply createReply(Reply reply) {
        // Vote 객체가 존재하는지 확인
        Vote vote = voteRepository.findById(reply.getVote().getVoteId())
                .orElseThrow(() -> new RuntimeException("Vote not found"));
        
        // Reply에 Vote 설정
        reply.setVote(vote);

        // Reply 저장
        return replyRepository.save(reply);
    }

    public Reply updateReply(int id, Reply replyDetails) {
        Reply reply = replyRepository.findById(id).orElse(null);

        if (reply != null) {
            reply.setContent(replyDetails.getContent());
            reply.setLevel(replyDetails.getLevel());
            reply.setReplyCreate(replyDetails.getReplyCreate());
            reply.setReplyModify(replyDetails.getReplyModify());
            reply.setParentReId(replyDetails.getParentReId());
            reply.setVote(replyDetails.getVote());
            reply.setMember(replyDetails.getMember());
            return replyRepository.save(reply);
        } else {
            return null;
        }
    }

    public void deleteReply(int id) {
        replyRepository.deleteById(id);
    }
}
