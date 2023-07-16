package dblab.sharing_flatform.factory.member;

import dblab.sharing_flatform.domain.embedded.address.Address;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.role.Role;

import java.util.List;

public class MemberFactory {

    public static Member createMember() {
        return new Member("username", "password", "phoneNum", createAddress(), "None", List.of(), List.of());
    }

    public static Member createMemberWithRoles(List<Role> roles) {
        return new Member("username", "password", "phoneNum", createAddress(),"None", roles, List.of());
    }

    public static Member createSendMember() {
        return new Member("sender", "", "", createAddress(), "None",List.of(), List.of());
    }

    public static Member createReceiveMember() {
        return new Member("receiver", "", "", createAddress(), "None",List.of(), List.of());
    }

    public static Address createAddress() {
        return new Address("city1", "district1", "street1", "zipCode1");
    }

    public static Member createRenderMember() {
        return new Member("빌려주는사람", "", "", createAddress(), "None", List.of(), List.of());
    }

    public static Member createBorrowerMember() {
        return new Member("빌리는사람", "", "", createAddress(), "None", List.of(), List.of());
    }

}
