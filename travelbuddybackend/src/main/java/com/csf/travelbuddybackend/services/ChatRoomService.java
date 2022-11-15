package com.csf.travelbuddybackend.services;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csf.travelbuddybackend.models.ChatRoom;
import com.csf.travelbuddybackend.repositories.ChatRoomRepository;

@Service
public class ChatRoomService {
    @Autowired
    private ChatRoomRepository chatRmRepo;

    public Optional<String> getChatId(
        String sender, String recipient, boolean createIfNotExist) throws ParseException {
            return chatRmRepo.getChatId(sender, recipient, createIfNotExist);
    }

    public List<ChatRoom> findChatRooms(String sender) throws ParseException{
        return chatRmRepo.findChatRooms(sender);
    }
    
}
