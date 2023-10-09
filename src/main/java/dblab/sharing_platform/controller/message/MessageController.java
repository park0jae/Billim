package dblab.sharing_platform.controller.message;

import dblab.sharing_platform.dto.message.MessageCreateRequest;
import dblab.sharing_platform.dto.message.MessagePagingCondition;
import dblab.sharing_platform.service.message.MessageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

import static dblab.sharing_platform.config.security.util.SecurityUtil.getCurrentUsernameCheck;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @ApiOperation(value = "메세지 생성 및 전송", notes = "메세지를 생성하고 수신자에게 전송합니다.")
    @PostMapping
    public ResponseEntity sendMessageToReceiverByCurrentUser(@Valid @RequestBody MessageCreateRequest messageCreateRequest) {
        return new ResponseEntity(messageService.sendMessageToReceiverByCurrentUser(messageCreateRequest, getCurrentUsernameCheck()), CREATED);
    }

    @ApiOperation(value = "메세지 단건 조회", notes = "메시지를 ID를 통해 단건 조회합니다.(해당 메시지의 소유자, 즉 수신자 및 발신자만 접근 가능)")
    @GetMapping("/{messageId}")
    public ResponseEntity findOneMessagesById(@PathVariable Long messageId){
        return new ResponseEntity(messageService.findMessageById(messageId), OK);
    }

    @ApiOperation(value = "송신 메시지 조회", notes = "현재 로그인한 유저가 송신한 메시지를 모두 조회합니다.")
    @GetMapping("/sent")
    public ResponseEntity findSendMessageByCurrentUser(@Valid MessagePagingCondition condition){
        condition.setSenderUsername(getCurrentUsernameCheck());
        return new ResponseEntity(messageService.findSendMessageByCurrentUser(condition), OK);
    }

    @ApiOperation(value = "수신 메시지 조회", notes = "현재 로그인한 유저가 수신한 메시지를 모두 조회합니다.")
    @GetMapping("/received")
    public ResponseEntity findReceiveMessageByCurrentUser(@Valid MessagePagingCondition condition){
        condition.setReceiverUsername(getCurrentUsernameCheck());
        return new ResponseEntity(messageService.findReceiveMessageByCurrentUser(condition), OK);
    }

    @ApiOperation(value = "송신자에 의한 메세지 삭제", notes = "메세지를 보낸 유저가 메세지를 삭제합니다.")
    @DeleteMapping("/{messageId}/sent")
    public ResponseEntity deleteMessageBySender(@ApiParam(value = "삭제할 Message Id", required = true) @PathVariable Long messageId) {
        messageService.deleteMessageBySender(messageId);
        return new ResponseEntity(OK);
    }

    @ApiOperation(value = "수신자에 의한 메세지 삭제", notes = "메세지를 수신한 유저가 메세지를 삭제합니다.")
    @DeleteMapping("/{messageId}/received")
    public ResponseEntity deleteMessageByReceiver(@ApiParam(value = "삭제할 Message Id", required = true) @PathVariable Long messageId) {
        messageService.deleteMessageByReceiver(messageId);
        return new ResponseEntity(OK);
    }
}