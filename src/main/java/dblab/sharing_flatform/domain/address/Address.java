package dblab.sharing_flatform.domain.address;

import lombok.*;

import javax.persistence.Embeddable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String city; // 도시
    private String district; // 구
    private String street; // 상세 주소
    private String zipCode; // 우편번호

    // ex: 전주시 덕진구 금암xx 1길 12345
}
