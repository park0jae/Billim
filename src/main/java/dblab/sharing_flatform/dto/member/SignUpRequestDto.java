package dblab.sharing_flatform.dto.member;

import dblab.sharing_flatform.domain.address.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String phoneNumber;

    @NotNull
    private Address address;
}
