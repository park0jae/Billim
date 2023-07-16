package dblab.sharing_flatform.domain.embedded.address;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @ApiModelProperty(value = "city", required = true, example = "전주시")
    private String city; // 도시

    @ApiModelProperty(value = "disrict", required = true, example = "덕진구")
    private String district; // 구

    @ApiModelProperty(value = "city", required = true, example = "금암동 1길")
    private String street; // 상세 주소

    @ApiModelProperty(value = "zipCode", required = true, example = "54475")
    private String zipCode; // 우편번호

}
