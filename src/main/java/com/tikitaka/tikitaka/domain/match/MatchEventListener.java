package com.tikitaka.tikitaka.domain.match;

import com.tikitaka.tikitaka.domain.match.event.MatchLikeEvent;
import com.tikitaka.tikitaka.domain.match.event.MatchOpenEvent;
import com.tikitaka.tikitaka.domain.match.event.MatchResponseEvent;
import com.tikitaka.tikitaka.domain.member.entity.Member;
import com.tikitaka.tikitaka.global.util.CustomStringUtil;
import com.tikitaka.tikitaka.infra.notification.NotificationRepository;
import com.tikitaka.tikitaka.infra.notification.NotificationService;
import com.tikitaka.tikitaka.infra.notification.constant.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;

// EventListener
@Component
@Async("match")
@RequiredArgsConstructor
@Transactional
public class MatchEventListener {
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    /**
     * 호감 보내기에 대한 이벤트
     * @param matchLikeEvent 수신 송신 멤버를 담은 DTO
     */
    @EventListener
    public void handleLikeEvent(MatchLikeEvent matchLikeEvent) {
        final Member sender = matchLikeEvent.getSender();
        final String senderHideName = CustomStringUtil.hideName(sender.getName());
        final Member receiver = matchLikeEvent.getReceiver();
        final String receiverFcmToken = receiver.getFcmToken();

        final String title = "호감 전달";
        final String content = senderHideName + "이(가) 너가 마음에 든대 🎉";

        //FcmToken 비어있으면 로그아웃 또는 푸시알림 거부 -> 푸시 알림을 보내지 않음
        if (!receiverFcmToken.isBlank() && receiver.isAcceptsPush()) {
            notificationService.sendByToken(receiverFcmToken, title, content);
        }

        //알림 히스토리는 저장
        notificationService.createNotification(receiver.getId(), title, content, NotificationType.MATCH);
    }

    /**
     * 호감 수락 및 거절에 대한 이벤트
     * @param matchResponseEvent 수신 송신 멤버를 담은 DTO
     */
    @EventListener
    public void handleResponseEvent(MatchResponseEvent matchResponseEvent) {
        final Member sender = matchResponseEvent.getSender();
        final String senderFcmToken = sender.getFcmToken();
        final Member receiver = matchResponseEvent.getReceiver();
        final String receiverHideName = CustomStringUtil.hideName(receiver.getName());

        final String title = matchResponseEvent.isAccepted() ? "호감 수락" : "호감 거절";
        final String content = receiverHideName +
                (matchResponseEvent.isAccepted() ? "도 너가 좋대 🎉" : "이(가) 너의 호감을 정중히 거절했어");

        if (!senderFcmToken.isBlank() && sender.isAcceptsPush()) {
            notificationService.sendByToken(senderFcmToken, title, content);
        }

        notificationService.createNotification(sender.getId(), title, content, NotificationType.MATCH);
    }


    /**
     * 번호 오픈 이벤트
     * @param matchOpenEvent 수신 송신 멤버를 담은 DTO
     */
    @EventListener
    public void handleOpenEvent(MatchOpenEvent matchOpenEvent) {
        final Member opener = matchOpenEvent.getOpener();
        final String openerFcmToken = opener.getFcmToken();
        final Member opposite  = matchOpenEvent.getOpposite();
        final String oppositeFcmToken  = opposite.getFcmToken();

        //번호 오픈 이후부터는 이름을 가리지 않음
        final String title = "호감 수락";
        final String content = opener.getName() + "이(가) 번호를 오픈했어!";

        if (!openerFcmToken.isBlank() && opener.isAcceptsPush()) {
            notificationService.sendByToken(openerFcmToken, title, content);
        }
        if (!oppositeFcmToken.isBlank() && opposite.isAcceptsPush()) {
            notificationService.sendByToken(oppositeFcmToken, title, content);
        }

        notificationService.createNotificationList(
                Arrays.asList(opener.getId(), opposite.getId()), title, content, NotificationType.MATCH);
    }

//
//    @EventListener
//    public void handleMatchCompleteEvent(MatchCompleteEvent matchCompleteEvent){
//
//        // 알림 보낼 멤버 목록
//        List<Member> memberList = matchCompleteEvent.getMemberList();
//
//        // 로그아웃 안한 회원의 fcmToken 뽑기
//        // 로그아웃한 회원들의 fcmToken 필드는 "" 공백입니다.
//        List<String> fcmTokenList = memberList
//                .stream()
//                .map(Member::getFcmToken)
//                .filter(fcmToken -> !fcmToken.isBlank()).collect(Collectors.toList());
//
//        // 로그아웃 안한 대상에게 알림 보내기
//        if (fcmTokenList.size() != 0){
//            notificationService.sendByTokenList(fcmTokenList);
//        }
//
//        // 알림 엔티티 생성 //
//        List<Notification> notificationList = memberList
//                .stream()
//                .map(member -> Notification.builder()
//                        .type(NotificationType.MATCH)
//                        .title("매칭 알림")
//                        .content("매칭이 성사되었습니다")
//                        .member(member)
//                        .build())
//                .collect(Collectors.toList());
//        // 알림 벌크 저장
////        notificationRepository.insertNotificationWithTeamId(notificationList);
//    }

}
