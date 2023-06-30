package dblab.sharing_flatform;

import dblab.sharing_flatform.domain.role.Role;
import dblab.sharing_flatform.domain.role.RoleType;
import dblab.sharing_flatform.repository.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final RoleRepository roleRepository;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void initDB() {
        initRoles();
    }

    @Transactional
    public void initRoles() {
        RoleType[] values = RoleType.values();
        List<Role> roles = Arrays.stream(values).map(roleType -> new Role(roleType)).collect(Collectors.toList());
        roleRepository.saveAll(roles);
    }

}
