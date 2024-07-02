package com.lec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lec.entity.Reply;
import com.lec.service.ReplyService;

@RestController
@RequestMapping("/api/replies")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @GetMapping
    public List<Reply> getAllReplies() {
        return replyService.getAllReplies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reply> getReplyById(@PathVariable int id) {
        Reply reply = replyService.getReplyById(id);
        return reply != null ? ResponseEntity.ok(reply) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{voteId}/replies")
    public Page<Reply> getRepliesByVoteId(
        @PathVariable("voteId") int voteId,
        Pageable pageable
    ) {
        return replyService.getRepliesByVoteId(voteId, pageable);
    }

    @PostMapping("/{voteId}")
    public ResponseEntity<Reply> addReplyToVote(
            @PathVariable("voteId") int voteId,
            @RequestBody Reply newReply) {

        Reply savedReply = replyService.addReplyToVote(voteId, newReply);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReply);
    }

    @PutMapping("/{replyId}")
    public ResponseEntity<Reply> updateReply(@PathVariable("replyId") int replyId, @RequestBody Reply replyDetails) {
        Reply updatedReply = replyService.updateReply(replyId, replyDetails);
        return updatedReply != null ? ResponseEntity.ok(updatedReply) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable("replyId") int replyId) {
        replyService.deleteReply(replyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public List<Reply> getRepliesByUserId(@PathVariable String userId) {
        // 로그 추가
        System.out.println("Fetching replies for user: " + userId);
        List<Reply> replies = replyService.getRepliesByUserId(userId);
        // 로그 추가
        System.out.println("Replies fetched: " + replies.size());
        return replies;
    }
}
