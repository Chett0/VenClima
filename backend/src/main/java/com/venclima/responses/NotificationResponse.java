package com.venclima.responses;

import com.venclima.dto.IslandNotificationDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private List<IslandNotificationDTO> notifications;
    private Boolean isActiveNotifications;

}
