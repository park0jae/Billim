package dblab.sharing_flatform.domain.member;

import dblab.sharing_flatform.domain.address.Address;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.role.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    private String phoneNumber;

    @Embedded
    private Address address;

    // role
    @OneToMany
    @JoinColumn(name = "role_id")
    private List<Role> roles = new ArrayList<>();

    // post
    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();


}
