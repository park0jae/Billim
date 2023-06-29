package dblab.sharing_flatform.domain.member;


import dblab.sharing_flatform.domain.role.Role;
import lombok.Getter;

import javax.persistence.*;

@Entity
@IdClass(MemberRoleId.class)
public class MemberRole {

    @Id
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
