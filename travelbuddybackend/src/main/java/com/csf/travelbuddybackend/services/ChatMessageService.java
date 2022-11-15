package com.csf.travelbuddybackend.services;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csf.travelbuddybackend.models.ChatMessage;
import com.csf.travelbuddybackend.repositories.ChatMessageRespository;

@Service
public class ChatMessageService {
    @Autowired
    private ChatMessageRespository chatMsgRepo;

    public ChatMessage save(ChatMessage chatMessage){
        return chatMsgRepo.save(chatMessage);
    }

    public List<ChatMessage> findChatMessages(String sender,String recipient) throws ParseException {
        return chatMsgRepo.findChatMessages(sender,recipient);
    }

    public ChatMessage findById(String id) throws ParseException {
        return chatMsgRepo.findById(id);
    }
}
