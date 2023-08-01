package dblab.sharing_flatform.factory.member;

import dblab.sharing_flatform.domain.embedded.address.Address;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.role.Role;
import dblab.sharing_flatform.domain.role.RoleType;
import dblab.sharing_flatform.repository.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MemberFactory {
    public static Member createMember() {
        return new Member("username", "password", "nickname","phoneNum", createAddress(), "None", List.of());
    }

    public static Member createMember(String username) {
        return new Member(username, "password","nickname", "phoneNum", createAddress(), "None", List.of());
    }

    public static Member createMemberWithRole() {
        return new Member("username", "password", "nickname", "phoneNum", createAddress(), "None", List.of(new Role(RoleType.USER)));
    }

    public static Member createMemberWithRoles(List<Role> roles) {
        return new Member("username", "password","nickname", "phoneNum", createAddress(),"None", roles);
    }

    public static Member createSendMember() {
        return new Member("sender", "", "sender1", "", createAddress(), "None", List.of());
    }

    public static Member createReceiveMember() {
        return new Member("receiver", "", "receiver1", "", createAddress(), "None", List.of());
    }

    public static Address createAddress() {
        return new Address("city1", "district1", "street1", "zipCode1");
    }

    public static Member createRenderMember() {
        return new Member("빌려주는사람", "", "render", "", createAddress(), "None", List.of());
    }

    public static Member createBorrowerMember() {
        return new Member("빌리는사람", "", "borrower", "", createAddress(), "None", List.of());
    }

}
