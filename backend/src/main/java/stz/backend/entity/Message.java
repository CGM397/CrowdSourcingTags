package stz.backend.entity;


import java.util.Date;

public class Message {
    String messageId;
    String userId;
    Date releaseTime;
    String title;
    String content;
    boolean isRead;

    public Message(){}

    public Message(String messageId, String userId, Date releaseTime, String title,
                   String content, boolean isRead){
        this.messageId = messageId;
        this.userId = userId;
        this.releaseTime = releaseTime;
        this.title = title;
        this.content = content;
        this.isRead = isRead;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getUserId() {
        return userId;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsRead(boolean read) {
        this.isRead = read;
    }
}
