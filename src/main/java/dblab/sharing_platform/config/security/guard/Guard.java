package dblab.sharing_platform.config.security.guard;

import dblab.sharing_platform.domain.role.RoleType;
import dblab.sharing_platform.config.security.util.SecurityUtil;

public abstract class Guard {

    public final boolean check(Long id){
       return SecurityUtil.isAuthenticated() && hasAuthority(id);
    }

    private boolean hasAuthority(Long id){
        return SecurityUtil.extractMemberRolesFromContext().contains(RoleType.ADMIN) || isResourceOwner(id);
    }

    abstract protected boolean isResourceOwner(Long id);
}
