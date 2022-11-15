package com.csf.travelbuddybackend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.domain.Sort;

import com.csf.travelbuddybackend.models.ChatMessage;
import com.mongodb.client.result.UpdateResult;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

@Repository
public class ChatMessageRespository {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired 
    private ChatRoomRepository chatRoomRepository;

    public ChatMessage save(ChatMessage chatMessage){
        Document toInsert = new Document();
        toInsert.append("chatId",chatMessage.getChatId());
        toInsert.append("sender",chatMessage.getSender());
        toInsert.append("recipient",chatMessage.getRecipient());
        toInsert.append("content",chatMessage.getContent());
        toInsert.append("timeStamp",new Date());
        toInsert.append("status","received");
        Document newDoc = mongoTemplate.insert(toInsert,"chatmessages");
        ObjectId id = newDoc.getObjectId("_id");
        
        Query query = Query.query(Criteria.where("chatId").is(chatMessage.getChatId()));
        Update updateOps = new Update()
            .set("lastMessage", chatMessage.getContent())
            .set("lastMessageTimeStamp",new Date());
        UpdateResult updateResult = mongoTemplate.updateMulti(query, updateOps, Document.class,"chatrooms");

        chatMessage.setId(id.toHexString());
        return chatMessage;
    }

    public List<ChatMessage> findByChatId(String cId) throws ParseException{
        Query query = Query.query(
            Criteria.where("chatId").is(cId)
            ).with(
                Sort.by(Sort.Direction.ASC,"timeStamp")
                );
        List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
        List<Document> result = mongoTemplate.find(query, Document.class,"chatmessages");
        for (int i = 0; i < result.size(); i++) {
            String json = result.get(i).toJson();
            chatMessages.add(ChatMessage.createRetrieve(json));
        }
        return chatMessages;
    }

    public List<ChatMessage> findChatMessages(String sender,String recipient) throws ParseException {
        var chatId = chatRoomRepository.getChatId(sender, recipient, false);

        var messages =
                chatId.map(cId -> {
                    try {
                        return findByChatId(cId);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).orElse(new ArrayList<>());

        if(messages.size() > 0) {
            updateStatuses(recipient, sender, "delivered");
        }

        return messages;
    }

    public ChatMessage findById(String id) throws ParseException {
        ChatMessage chatMessage = findByObjectId(id);
        chatMessage.setStatus("delivered");
        ObjectId docId = new ObjectId(id);
        Query query = Query.query(
            Criteria.where("_id").is(docId));
        Update update = new Update().set("status", "delivered");
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, Document.class,"chatmessages");
        return chatMessage;
    }

    public ChatMessage findByObjectId(String id) throws ParseException{
        ObjectId docId = new ObjectId(id);
        Document result = mongoTemplate.findById(docId, Document.class,"chatmessages");
        String json = result.toJson();
        ChatMessage chatMessage = ChatMessage.createRetrieve(json);
        return chatMessage;
    }

    public void updateStatuses(String sender, String recipient, String status) {
        Query query = Query.query(
                Criteria
                        .where("sender").is(sender)
                        .and("recipient").is(recipient));
        Update update = new Update().set("status", status);
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, Document.class,"chatmessages");
    }

}
