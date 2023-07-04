package dblab.sharing_flatform.controller.message;

import dblab.sharing_flatform.aop.AssignUsername;
import dblab.sharing_flatform.dto.message.MessageRequestDto;
import dblab.sharing_flatform.dto.message.MessageResponseDto;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/{receiverId}")
    public Response sendMessageToReceiver(@PathVariable Long receiverId ,@RequestBody MessageRequestDto messageRequestDto){
        // 유저가 존재하고, 둘다 deleteBy 가 false 이면 성공하도록
        MessageResponseDto messageResponseDto = messageService.sendMessage(messageRequestDto);
        return Response.success(messageResponseDto);
    }

}