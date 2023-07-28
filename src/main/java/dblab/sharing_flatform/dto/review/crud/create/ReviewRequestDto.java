package dblab.sharing_flatform.dto.review.crud.create;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewRequestDto {

    private String content;

    private Double starRating;

}
