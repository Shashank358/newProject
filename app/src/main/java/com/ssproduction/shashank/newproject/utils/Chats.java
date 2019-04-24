package com.ssproduction.shashank.newproject.utils;

public class Chats {

    private String sender;
    private String receiver;
    private String message;
    private boolean isseen;
    private String msg_type;
    private String sent_time;

    public Chats(String sender, String receiver, String message, boolean isseen,
                 String msg_type, String sent_time) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isseen = isseen;
        this.msg_type = msg_type;
        this.sent_time = sent_time;
    }

    public Chats(){

    }

    public String getSent_time() {
        return sent_time;
    }

    public void setSent_time(String sent_time) {
        this.sent_time = sent_time;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() { return receiver; }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
