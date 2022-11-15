package com.csf.travelbuddybackend.models;

public class ChatNotification {
    private String id;
    private String sender;

    public ChatNotification(String id,String sender) {
        this.id = id;
        this.sender = sender;
      }
    
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
}
