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

public class ChatMessage {
    private String id;
    private String chatId;
    private String sender;
    private String recipient;
    private String content;
    private String timeStamp;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static ChatMessage createSave(String json) throws ParseException {
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject data = reader.readObject();

        final ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRecipient(data.getString("recipient"));
        chatMessage.setSender(data.getString("sender"));
        chatMessage.setContent(data.getString("content"));
        return chatMessage;

    }

    public static ChatMessage createRetrieve(String json) throws ParseException {
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject data = reader.readObject();

        final ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRecipient(data.getString("recipient"));
        chatMessage.setSender(data.getString("sender"));
        chatMessage.setContent(data.getString("content"));
        System.out.println(">>>>>>>>>>>>>>data"+data);
        try {
            if (data.getJsonObject("timeStamp").getString("$date") != null) {
                String s = data.getJsonObject("timeStamp").getString("$date");
                TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(s);
                Instant i = Instant.from(ta);
                Date d = Date.from(i);

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                String b = formatter.format(d);
                chatMessage.setTimeStamp(b);
                System.out.println(">>>>>>>>>>>>>>>>timestamp"+ b);
            }
        } catch (NullPointerException e) {
            System.out.println(e);
        }
        return chatMessage;

    }
}
