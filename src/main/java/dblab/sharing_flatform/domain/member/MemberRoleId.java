package dblab.sharing_flatform.domain.member;

import java.io.Serializable;

public class MemberRoleId implements Serializable {

    private Long member;
    private Long role;

    public MemberRoleId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberRoleId)) return false;

        MemberRoleId that = (MemberRoleId) o;

        if (member != null ? !member.equals(that.member) : that.member != null) return false;
        return role != null ? role.equals(that.role) : that.role == null;
    }

    @Override
    public int hashCode() {
        int result = member != null ? member.hashCode() : 0;
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

}
