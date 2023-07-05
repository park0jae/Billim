package dblab.sharing_flatform.controller.message;

import dblab.sharing_flatform.dto.message.MessageRequestDto;
import dblab.sharing_flatform.dto.message.MessageResponseDto;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.service.message.MessageService;
import dblab.sharing_flatform.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/send")
    public Response findSendMessage(){
        String senderName = SecurityUtil.getCurrentUsername().orElseThrow(AccessDeniedException::new);
        List<MessageResponseDto> sendMessage = messageService.findSendMessage(senderName);

        return Response.success(sendMessage);
    }

    @GetMapping("/receive")
    public Response findReceiveMessage(){
        String receiverName = SecurityUtil.getCurrentUsername().orElseThrow(AccessDeniedException::new);
        List<MessageResponseDto> receiveMessage = messageService.findReceiveMessage(receiverName);

        return Response.success(receiveMessage);
    }

    @GetMapping("/send/{id}")
    public Response findSendMessageToMember(@PathVariable Long id){
        String senderName = SecurityUtil.getCurrentUsername().orElseThrow(AccessDeniedException::new);
        List<MessageResponseDto> sendMessageToMember = messageService.findSendMessageToMember(senderName, id);

        return Response.success(sendMessageToMember);
    }

    @GetMapping("/receive/{id}")
    public Response findReceiveMessageByMember(@PathVariable Long id){
        String receiverName = SecurityUtil.getCurrentUsername().orElseThrow(AccessDeniedException::new);
        List<MessageResponseDto> sendMessageToMember = messageService.findReceiveMessageByMember(receiverName, id);

        return Response.success(sendMessageToMember);
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