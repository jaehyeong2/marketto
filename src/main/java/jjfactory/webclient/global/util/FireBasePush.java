package jjfactory.webclient.global.util;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jjfactory.webclient.global.dto.req.FcmMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class FireBasePush {
    private final PasswordEncoder passwordEncoder;
    @Value("${fireBase.path}")
    private String path;

    @PostConstruct
    public void init() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(
                            GoogleCredentials
                                    .fromStream(new FileInputStream(path)))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    @Async
    public void sendMessage(FcmMessageDto dto){
        log.info("firebase token :{}",dto.getFcmToken());
        try {
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(dto.getTitle())
                            .setBody(dto.getContent())
                            .build())
                    .setToken(dto.getFcmToken())
                    .build();

            ApiFuture<String> response = FirebaseMessaging.getInstance().sendAsync(message);
            log.info("Successfully sent message: {}",response.get());
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
