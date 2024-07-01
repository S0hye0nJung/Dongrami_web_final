package com.lec.dto;

import java.time.LocalDate;

public class MyReplyDTO {
    private int replyId;
    private String content;
    private String question;
    private LocalDate replyCreate;
    private int voteId;

    public MyReplyDTO(int replyId, String content, String question, LocalDate replyCreate, int voteId) {
        this.replyId = replyId;
        this.content = content;
        this.question = question;
        this.replyCreate = replyCreate;
        this.voteId = voteId;
    }

    // Getters and Setters
    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public LocalDate getReplyCreate() {
        return replyCreate;
    }

    public void setReplyCreate(LocalDate replyCreate) {
        this.replyCreate = replyCreate;
    }

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }
}
