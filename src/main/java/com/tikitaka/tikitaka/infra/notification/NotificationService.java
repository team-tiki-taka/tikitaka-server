package com.tikitaka.tikitaka.infra.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.tikitaka.tikitaka.global.error.ErrorCode;
import com.tikitaka.tikitaka.global.error.exception.InternalServerException;
import com.tikitaka.tikitaka.infra.notification.constant.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Value("${fcm.key.path}")
    private String FCM_PRIVATE_KEY_PATH;

    //
    // 메시징만 권한 설정
    @Value("${fcm.key.scope}")
    private String fireBaseScope;

    // fcm 기본 설정 진행
    @PostConstruct
    public void init() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(
                            GoogleCredentials
                                    .fromStream(new ClassPathResource(FCM_PRIVATE_KEY_PATH).getInputStream())
                                    .createScoped(List.of(fireBaseScope)))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            // spring 뜰때 알림 서버가 잘 동작하지 않는 것이므로 바로 죽임
            throw new RuntimeException(e.getMessage());
        }
    }

    public void sendByToken(String token, String title, String content) {

        // 메시지 만들기
        Message message = Message.builder()
                .putData("time", LocalDateTime.now().toString())
                .setNotification(new Notification(title, content))
                .setToken(token)
                .build();

        try {
            // 알림 발송
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("cannot send to memberList push message. error info : {}", e.getMessage());
        }
    }

    // 알림 보내기
    public void sendByTokenList(List<String> tokenList, String title, String content) {

        // 메시지 만들기
        List<Message> messages = tokenList.stream().map(token -> Message.builder()
                .putData("time", LocalDateTime.now().toString())
                .setNotification(new Notification(title, content))
                .setToken(token)
                .build()).collect(Collectors.toList());

        // 요청에 대한 응답을 받을 response
        BatchResponse response;
        try {

            // 알림 발송
            response = FirebaseMessaging.getInstance().sendAll(messages);

            // 요청에 대한 응답 처리
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                List<String> failedTokens = new ArrayList<>();

                // response 내부에 error 가 포함되어 있음 (Exception 발생 x)
                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        failedTokens.add(tokenList.get(i));
                    }
                }
                log.error("List of tokens are not valid FCM token : " + failedTokens);
            }
        } catch (FirebaseMessagingException e) {
            log.error("cannot send to memberList push message. error info : {}", e.getMessage());
        }
    }

    public void createNotification(Long memberId, String title, String content, NotificationType notificationType) {
        try {
            notificationRepository.save(
                    com.tikitaka.tikitaka.infra.notification.entity.Notification.builder()
                            .title(title)
                            .content(content)
                            .memberId(memberId)
                            .type(notificationType)
                            .build());
        } catch (Exception e) {
            throw new InternalServerException(ErrorCode.CANNOT_CREATE_TUPLE);
        }
    }

    public void createNotificationList(List<Long> memberId, String title, String content, NotificationType notificationType) {
        try {
            notificationRepository.saveAll(memberId.stream().map(id ->
                com.tikitaka.tikitaka.infra.notification.entity.Notification.builder()
                        .title(title)
                        .content(content)
                        .memberId(id)
                        .type(notificationType)
                        .build()
            ).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new InternalServerException(ErrorCode.CANNOT_CREATE_TUPLE);
        }
    }
}
