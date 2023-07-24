package dblab.sharing_flatform.dto.notification.crud.create;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "알림 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationRequestDto {

    @ApiModelProperty(value = "알림 내용", notes = "알림 내용을 입력해주세요.", required = true)
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @ApiModelProperty(value = "알림 항목", notes = "어떤 항목에 대한 알림인지 명시해주세요.", required = true)
    @NotBlank(message = "알림 항목을 입력해주세요.")
    private String notificationType;

    private String receiver;



}
