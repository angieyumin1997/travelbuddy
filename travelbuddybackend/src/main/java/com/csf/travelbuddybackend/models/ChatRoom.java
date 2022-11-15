package com.csf.travelbuddybackend.models;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class ChatRoom {
    private String id;
    private String chatId;
    private String sender;
    private String recipient;
    private String lastMessage;
    private String lastMessageTimeStamp;
    private String newMessageCount;

    public String getNewMessageCount() {
        return newMessageCount;
    }

    public void setNewMessageCount(String newMessageCount) {
        this.newMessageCount = newMessageCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getLastMessageTimeStamp() {
        return lastMessageTimeStamp;
    }

    public void setLastMessageTimeStamp(String lastMessageTimeStamp) {
        this.lastMessageTimeStamp = lastMessageTimeStamp;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public static ChatRoom create(String json) throws ParseException {
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject data = reader.readObject();
        final ChatRoom chatRoom = new ChatRoom();

        chatRoom.setRecipient(data.getString("recipient"));
        chatRoom.setSender(data.getString("sender"));
        chatRoom.setChatId(data.getString("chatId"));
        try {
            if (data.getString("lastMessage") != null) {
                String s = data.getJsonObject("lastMessageTimeStamp").getString("$date");
                TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(s);
                Instant i = Instant.from(ta);
                Date d = Date.from(i);

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");  
                String b = formatter.format(d);  

                chatRoom.setLastMessageTimeStamp(b);
                chatRoom.setLastMessage(data.getString("lastMessage"));
            }
        } catch (NullPointerException e) {
            System.out.println(e);
        }
        return chatRoom;
    }
}
