package dblab.sharing_flatform.controller.message;

import dblab.sharing_flatform.dto.message.MessageRequestDto;
import dblab.sharing_flatform.dto.message.MessageResponseDto;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.service.message.MessageService;
import dblab.sharing_flatform.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public Response sendMessageToReceiver(@RequestBody MessageRequestDto messageRequestDto) {
        String username = SecurityUtil.getCurrentUsername().orElseThrow(AccessDeniedException::new);
        messageRequestDto.setSendMember(username);

        MessageResponseDto messageResponseDto = messageService.sendMessage(messageRequestDto);
        return Response.success(messageResponseDto);
    }

    @DeleteMapping("/send/{id}")
    public Response deleteBySender(@PathVariable Long id) {
        // 보낸 메세지가 자신의 소유인지 확인 - messageGuard
        messageService.deleteMessageBySender(id);
        return Response.success();
    }

    @DeleteMapping("/receive/{id}")
    public Response deleteByReceiver(@PathVariable Long id) {
        messageService.deleteMessageByReceiver(id);
        return Response.success();
    }
}