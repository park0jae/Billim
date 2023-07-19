package dblab.sharing_flatform.dto.review.crud.create;

import lombok.*;

import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewRequestDto {

    @Null
    private String writer;

    private String content;

    private double starRating;

}
