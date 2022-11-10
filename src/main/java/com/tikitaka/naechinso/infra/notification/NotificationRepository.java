package com.tikitaka.naechinso.infra.notification;

import com.tikitaka.naechinso.infra.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
