package com.csf.travelbuddybackend.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.csf.travelbuddybackend.models.FirebaseWebNotification;
import com.csf.travelbuddybackend.models.MessageResponse;
import com.csf.travelbuddybackend.services.FirebaseWebNotificationService;
import com.google.firebase.messaging.FirebaseMessagingException;

@RestController
@RequestMapping("/api")
public class FirebaseWebNotificationRestController {
    @Autowired
    private FirebaseWebNotificationService fService;

    @PostMapping(path = "/sendnotification")
    public ResponseEntity<?> sendNotification(@RequestParam String recipient, @RequestParam String sender, @RequestParam String message) throws FirebaseMessagingException  {
        FirebaseWebNotification fNotification = new FirebaseWebNotification();
        fNotification.setTitle(sender);
        fNotification.setMessage(message);
        Optional<String> token = fService.getToken(recipient);
        if (token.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }else{
            fNotification.setTarget(token.get());
            return ResponseEntity.ok(fService.sendNotification(fNotification));
        }
    }

    @PostMapping(path = "/savetoken")
    public ResponseEntity<MessageResponse> saveToken(@RequestParam String token){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        fService.insertToken(token, userDetails.getUsername());
        return ResponseEntity.ok(new MessageResponse("Token is saved successfully"));
    }
}
