package com.lec.service;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lec.dto.ReplyDTO;
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

    public ReplyDTO createReply(ReplyDTO replyDTO) {
        Reply reply = new Reply(
                replyDTO.getReplyId(),
                replyDTO.getContent(),
                replyDTO.getLevel(),
                replyDTO.getReplyCreate(),
                replyDTO.getReplyModify(),
                replyDTO.getParentReId(),
                // vote와 member는 이 예제에서 단순화를 위해 null로 설정
                null, 
                null
        );
        reply = replyRepository.save(reply);
        return new ReplyDTO(reply);
    }
    public Reply addReplyToVote(int voteId, Reply newReply) {
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new RuntimeException("Vote not found"));
        newReply.setVote(vote);
        
        return replyRepository.save(newReply);
    }
    public Reply updateReply(int replyId, Reply replyDetails) {
        Reply reply = replyRepository.findById(replyId).orElse(null);
        
        if (reply != null) {
            System.out.println("Original Reply: " + reply);
            System.out.println("Update details: " + replyDetails);

            reply.setContent(replyDetails.getContent());
            reply.setLevel(replyDetails.getLevel());
            // reply.setReplyCreate(replyDetails.getReplyCreate()); // Generally not updated on edit
            reply.setReplyModify(LocalDate.now()); // Update to current time
            reply.setParentReId(replyDetails.getParentReId());
            reply.setVote(replyDetails.getVote());
            reply.setMember(replyDetails.getMember());
            
            Reply savedReply = replyRepository.save(reply);
            System.out.println("Saved Reply: " + savedReply);
            return savedReply;
        } else {
            System.out.println("Reply not found with id: " + replyId);
            return null;
        }
    }

    public void deleteReply(int replyId) {
        replyRepository.deleteById(replyId);
    }

}
