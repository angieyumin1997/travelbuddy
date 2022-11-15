package com.csf.travelbuddybackend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csf.travelbuddybackend.models.FirebaseWebNotification;
import com.csf.travelbuddybackend.repositories.FirebaseWebNotificationRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;

@Service
public class FirebaseWebNotificationService  {

    @Autowired
    private FirebaseWebNotificationRepository firebaseWebNotificationRepo;

    private final FirebaseMessaging firebaseMessaging;

    public FirebaseWebNotificationService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    public String sendNotification(FirebaseWebNotification notification) throws FirebaseMessagingException {
       
        Message message = Message.builder()
                        .setWebpushConfig(
                                WebpushConfig.builder()
                                        .setNotification(
                                                WebpushNotification.builder()
                                                        .setTitle(notification.getTitle())
                                                        .setBody(notification.getMessage())
                                                        .build()
                                        ).build()
                        )
                        .setToken(notification.getTarget())
                        .build();
        return firebaseMessaging.send(message);
    }

    public void insertToken(String token, String username){
        firebaseWebNotificationRepo.insertToken(token, username);
    }

    public Optional<String> getToken(String username){
        return firebaseWebNotificationRepo.getToken(username);
    }
}
