package dblab.sharing_flatform.dto.review;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewRequestDto {

    private String content;
}
