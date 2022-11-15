package com.csf.travelbuddybackend.repositories;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.csf.travelbuddybackend.models.ChatRoom;

@Repository
public class ChatRoomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Optional<String> getChatId(
            String sender, String recipient, boolean createIfNotExist) throws ParseException {

        return findBySenderIdAndRecipientId(sender, recipient)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (!createIfNotExist) {
                        return Optional.empty();
                    }
                    var chatId = String.format("%s_%s", sender, recipient);

                    ChatRoom senderRecipient = new ChatRoom();
                    senderRecipient.setChatId(chatId);
                    senderRecipient.setRecipient(recipient);
                    senderRecipient.setSender(sender);

                    ChatRoom recipientSender = new ChatRoom();
                    recipientSender.setChatId(chatId);
                    recipientSender.setRecipient(sender);
                    recipientSender.setSender(recipient);

                    save(senderRecipient);
                    save(recipientSender);

                    return Optional.of(chatId);
                });
    }

    public Optional<ChatRoom> findBySenderIdAndRecipientId(String sender, String recipient) throws ParseException {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("sender").is(sender),
                Criteria.where("recipient").is(recipient));
        Query query = Query.query(criteria);
        Document result = mongoTemplate.findOne(query, Document.class, "chatrooms");
        ChatRoom chatRoom = new ChatRoom();
        if (result != null) {
            String json = result.toJson();
            chatRoom = ChatRoom.create(json);
        }
        return Optional.of(chatRoom);
    }

    public ChatRoom save(ChatRoom chatRoom) {
        Document toInsert = new Document();
        toInsert.append("chatId", chatRoom.getChatId());
        toInsert.append("sender", chatRoom.getSender());
        toInsert.append("recipient", chatRoom.getRecipient());
        Document newDoc = mongoTemplate.insert(toInsert, "chatrooms");
        ObjectId id = newDoc.getObjectId("_id");
        return chatRoom;
    }

    public long countNewMessages(String sender,String recipient){
        Query query = Query.query(Criteria.where("status").is("received")
            .and("sender").is(recipient)
            .and("recipient").is(sender));
        long count = mongoTemplate.count(query, "chatmessages");
        return count;
    }

    public List<ChatRoom> findChatRooms(String sender) throws ParseException {
        Query query = Query.query(Criteria.where("sender").is(sender)).with(
                Sort.by(Sort.Direction.DESC, "lastMessageTimeStamp"));
        List<ChatRoom> chatRooms = new ArrayList<ChatRoom>();
        ChatRoom chatRoom = new ChatRoom();

        List<Document> results = mongoTemplate.find(query, Document.class, "chatrooms");
        for (int i = 0; i < results.size(); i++) {
            String json = results.get(i).toJson();
            chatRoom = ChatRoom.create(json);
            chatRoom.setNewMessageCount(
                Long.toString(
                    countNewMessages(
                        chatRoom.getSender(), 
                        chatRoom.getRecipient())));
            chatRooms.add(chatRoom);
        }
        return chatRooms;
    }
}
