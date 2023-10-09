package dblab.sharing_platform.domain.refresh;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String token;

    private RefreshToken(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public static RefreshToken createToken(String username, String token){
        return new RefreshToken(username, token);
    }

    public void changeToken(String token) {
        this.token = token;
    }
}
