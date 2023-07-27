package dblab.sharing_flatform.controller.message;

import dblab.sharing_flatform.dto.message.crud.create.MessageCreateRequestDto;
import dblab.sharing_flatform.dto.message.MessageDto;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.service.message.MessageService;
import dblab.sharing_flatform.config.security.util.SecurityUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    @ApiOperation(value = "메세지 생성 및 전송", notes = "메세지를 생성하고 수신자에게 전송합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response sendMessageToReceiver(@Valid @RequestBody MessageCreateRequestDto messageCreateRequestDto) {
        MessageDto messageDto = messageService.sendMessage(messageCreateRequestDto);
        return Response.success(messageDto);
    }

    @ApiOperation(value = "송신 메시지 조회", notes = "현재 로그인한 유저가 송신한 메시지를 모두 조회합니다.")
    @GetMapping("/send")
    @ResponseStatus(HttpStatus.OK)
    public Response findSendMessage(){
        String senderName = SecurityUtil.getCurrentUsername().orElseThrow(AccessDeniedException::new);
        List<MessageDto> sendMessage = messageService.findSendMessage(senderName);

        return Response.success(sendMessage);
    }

    @ApiOperation(value = "수신 메시지 조회", notes = "현재 로그인한 유저가 수신한 메시지를 모두 조회합니다.")
    @GetMapping("/receive")
    @ResponseStatus(HttpStatus.OK)
    public Response findReceiveMessage(){
        String receiverName = SecurityUtil.getCurrentUsername().orElseThrow(AccessDeniedException::new);
        List<MessageDto> receiveMessage = messageService.findReceiveMessage(receiverName);
        return Response.success(receiveMessage);
    }

    @ApiOperation(value = "특정 송신 메시지 조회", notes = "현재 로그인한 유저가 특정 유저에게 송신한 메세지를 조회합니다.")
    @GetMapping("/send/{receiverName}")
    @ResponseStatus(HttpStatus.OK)
    public Response findSendMessageToMember(@ApiParam(value = "수신자 이름", required = true) @PathVariable String receiverName){
        String senderName = SecurityUtil.getCurrentUsername().orElseThrow(AccessDeniedException::new);
        List<MessageDto> sendMessageToMember = messageService.findSendMessageToMember(senderName, receiverName);
        return Response.success(sendMessageToMember);
    }

    @ApiOperation(value = "특정 수신 메시지 조회", notes = "현재 로그인한 유저가 특정 유저로부터 수신한 메세지를 조회합니다.")
    @GetMapping("/receive/{senderName}")
    @ResponseStatus(HttpStatus.OK)
    public Response findReceiveMessageByMember(@ApiParam(value = "송신자 이름", required = true) @PathVariable String senderName){
        String username = SecurityUtil.getCurrentUsername().orElseThrow(AccessDeniedException::new);
        List<MessageDto> sendMessageToMember = messageService.findReceiveMessageFromMember(username, senderName);
        return Response.success(sendMessageToMember);
    }

    @ApiOperation(value = "송신자에 의한 메세지 삭제", notes = "메세지를 보낸 유저가 메세지를 삭제합니다.")
    @DeleteMapping("/send/{messageId}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteBySender(@ApiParam(value = "삭제할 Message Id", required = true) @PathVariable Long messageId) {
        messageService.deleteMessageBySender(messageId);
        return Response.success();
    }

    @ApiOperation(value = "수신자에 의한 메세지 삭제", notes = "메세지를 수신한 유저가 메세지를 삭제합니다.")
    @DeleteMapping("/receive/{messageId}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteByReceiver(@ApiParam(value = "삭제할 Message Id", required = true) @PathVariable Long messageId) {
        messageService.deleteMessageByReceiver(messageId);
        return Response.success();
    }
}