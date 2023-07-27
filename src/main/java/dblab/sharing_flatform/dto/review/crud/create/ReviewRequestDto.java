package dblab.sharing_flatform.dto.review.crud.create;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewRequestDto {

    @ApiModelProperty(hidden = true)
    @Null
    private String username;

    private String content;

    private Double starRating;

}
