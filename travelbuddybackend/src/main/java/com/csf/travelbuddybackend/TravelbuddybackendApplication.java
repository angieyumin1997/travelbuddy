package com.csf.travelbuddybackend;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class TravelbuddybackendApplication {

    @Value("${firebase.service.account}")
    private String firebaseCredentials;
	
    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        // GoogleCredentials googleCredentials = GoogleCredentials
        //         .fromStream(new ClassPathResource(firebase).getInputStream());
        InputStream googleCredentials = new ByteArrayInputStream(firebaseCredentials.getBytes());
        FirebaseOptions firebaseOptions = FirebaseOptions
                .builder()
                .setCredentials(GoogleCredentials.fromStream(googleCredentials))
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "travelbuddy");
        return FirebaseMessaging.getInstance(app);
    }

	public static void main(String[] args) {
		SpringApplication.run(TravelbuddybackendApplication.class, args);
	}

}