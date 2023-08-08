package dblab.sharing_platform.controller.notification;

import dblab.sharing_platform.config.security.util.SecurityUtil;
import dblab.sharing_platform.dto.response.Response;
import dblab.sharing_platform.service.notification.NotificationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @ApiOperation(value = "알림 구독", notes = "알림을 구독한다.")
    @GetMapping(value = "/subscription", produces = "text/event-stream")
    @ResponseStatus(HttpStatus.OK)
    public SseEmitter subscribeNotification(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribeNotification(Long.valueOf(SecurityUtil.getCurrentUserIdCheck()), lastEventId);
    }

    @ApiOperation(value = "알림 구독 취소", notes = "알림을 구독 취소한다.")
    @GetMapping(value = "/unsubscription")
    @ResponseStatus(HttpStatus.OK)
    public Response unSubscribeNotification() {
        notificationService.unSubscribeNotification(Long.valueOf(SecurityUtil.getCurrentUserIdCheck()));
        return Response.success();
    }
}
