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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/vote/{voteId}")
    public List<Reply> getRepliesByVoteId(@PathVariable("voteId") int voteId) {
        return replyService.getRepliesByVoteId(voteId);
    }

    @PostMapping("/comment/{voteId}")
    public ResponseEntity<Reply> createReply(@PathVariable("voteId") int voteId, @RequestBody Reply reply) {
        // voteId를 댓글에 설정
        reply.setVote(new Vote());
        reply.getVote().setVoteId(voteId);

        // 댓글 서비스를 통해 저장하고 생성된 댓글을 반환
        Reply createdReply = replyService.createReply(reply);
        return ResponseEntity.ok(createdReply);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Reply> updateReply(@PathVariable int id, @RequestBody Reply replyDetails) {
        Reply updatedReply = replyService.updateReply(id, replyDetails);
        return updatedReply != null ? ResponseEntity.ok(updatedReply) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReply(@PathVariable int id) {
        replyService.deleteReply(id);
        return ResponseEntity.noContent().build();
    }
}
