package com.csf.travelbuddybackend.controllers;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csf.travelbuddybackend.models.ChatMessage;
import com.csf.travelbuddybackend.models.ChatNotification;
import com.csf.travelbuddybackend.services.ChatMessageService;
import com.csf.travelbuddybackend.services.ChatRoomService;

@RestController
@RequestMapping("/api")
public class ChatRestController {
    @Autowired 
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageService chatMsgSvc;

    @Autowired
    private ChatRoomService chatRmSvc;

    @MessageMapping("/chat")
    public void processMessage(@Payload String json) throws ParseException {
        ChatMessage chatMessage = ChatMessage.createSave(json);
        var chatId = chatRmSvc
                .getChatId(chatMessage.getSender(), chatMessage.getRecipient(), true);
        chatMessage.setChatId(chatId.get());
        ChatMessage saved = chatMsgSvc.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipient(),"/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getSender()));
    }

    @GetMapping("/messages/{sender}/{recipient}")
    public ResponseEntity<?> findChatMessages ( @PathVariable String sender,
                                                @PathVariable String recipient) throws ParseException {
        return ResponseEntity
                .ok(chatMsgSvc.findChatMessages(sender, recipient));
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<?> findMessage ( @PathVariable String id) throws ParseException {
        return ResponseEntity
                .ok(chatMsgSvc.findById(id));
    }

    @GetMapping("/chats/{sender}")
    public ResponseEntity<?> findChat ( @PathVariable String sender) throws ParseException {
        return ResponseEntity
                .ok(chatRmSvc.findChatRooms(sender));
    }

    @GetMapping("/createchat/{sender}/{recipient}")
    public ResponseEntity<?> createChat ( @PathVariable String sender,
                                          @PathVariable String recipient) throws ParseException {
        return ResponseEntity
                .ok(chatRmSvc.getChatId(sender,recipient,true));
    }

}
