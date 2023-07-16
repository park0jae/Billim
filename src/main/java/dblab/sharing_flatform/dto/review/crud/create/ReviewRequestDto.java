package dblab.sharing_flatform.dto.review.crud.create;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewRequestDto {

    private String content;
    private double starRating;

}
