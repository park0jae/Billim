package dblab.sharing_platform.domain.embedded.address;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Embeddable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @ApiModelProperty(value = "도시", required = true, example = "전주시")
    private String city;

    @ApiModelProperty(value = "구", required = true, example = "덕진구")
    private String district;

    @ApiModelProperty(value = "도로명 주소", required = true, example = "금암동 1길")
    private String street;

    @ApiModelProperty(value = "우편번호", required = true, example = "54475")
    private String zipCode;

}
