package com.tikitaka.naechinso.domain.notification;

import com.tikitaka.naechinso.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
