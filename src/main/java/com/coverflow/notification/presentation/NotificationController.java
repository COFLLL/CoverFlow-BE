package com.coverflow.notification.presentation;

import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.notification.application.NotificationService;
import com.coverflow.notification.dto.request.UpdateNotificationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/notification")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @MemberAuthorize
    public ResponseEntity<SseEmitter> connect(
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") final String lastEventId,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        return ResponseEntity.ok()
//                .body(ResponseHandler.<SseEmitter>builder()
//                        .statusCode(HttpStatus.OK)
//                        .data(notificationService.connect(userDetails.getUsername(), lastEventId))
//                        .build()
//                );
        return ResponseEntity.ok(notificationService.connect(userDetails.getUsername(), lastEventId));
    }

//    @GetMapping
//    public ResponseEntity<ResponseHandler<List<FindNotificationResponse>>> findNotification(
//            @AuthenticationPrincipal final UserDetails userDetails
//    ) {
//        return ResponseEntity.ok()
//                .body(ResponseHandler.<List<FindNotificationResponse>>builder()
//                        .statusCode(HttpStatus.OK)
//                        .data(notificationService.findNotification(userDetails.getUsername()))
//                        .build()
//                );
//    }

    @PatchMapping
    public ResponseEntity<ResponseHandler<Void>> update(
            @RequestBody @Valid final List<UpdateNotificationRequest> requests
    ) {
        notificationService.update(requests);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build()
                );
    }
}