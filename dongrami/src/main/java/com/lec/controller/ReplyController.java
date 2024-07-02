//package com.lec.controller;
//
//import com.lec.dto.ReplyDTO;
//import com.lec.entity.Reply;
//import com.lec.service.ReplyService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//public class ReplyController {
//
//    @Autowired
//    private ReplyService replyService;
//    private ReplyDTO ReplyDTO;
//
//    @GetMapping("/vote/{voteId}/replies")
//    public String getReplies(@PathVariable int voteId, Model model) {
//        List<Reply> replies = replyService.getRepliesForVote(voteId);
//        List<ReplyDTO> replyTree = replyService.buildReplyTree(replies);
//        model.addAttribute("replies", replyTree);
//        return "replyView";
//    }
//
//    @PostMapping("/reply/add")
//    public String addReply(@ModelAttribute Reply reply) {
//        replyService.addReply(reply);
//        return "redirect:/vote/" + reply.getVote().getVoteId() + "/replies";
//    }
//}
package com.lec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lec.dto.ReplyDTO;
import com.lec.entity.Reply;
import com.lec.entity.Vote;
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

	/*
	 * @GetMapping("/vote/{voteId}") public List<Reply>
	 * getRepliesByVoteId(@PathVariable("voteId") int voteId) { return
	 * replyService.getRepliesByVoteId(voteId); }
	 */
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

        // 특정 투표에 댓글 추가하기
        Reply savedReply = replyService.addReplyToVote(voteId, newReply);

        // 새로 작성된 댓글과 함께 201 Created 응답 반환
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
}
