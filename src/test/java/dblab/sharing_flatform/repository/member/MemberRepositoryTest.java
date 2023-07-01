package dblab.sharing_flatform.repository.member;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.role.Role;
import dblab.sharing_flatform.domain.role.RoleType;
import dblab.sharing_flatform.exception.role.RoleNotFoundException;
import dblab.sharing_flatform.repository.role.RoleRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static dblab.sharing_flatform.factory.member.MemberFactory.*;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RoleRepository roleRepository;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    public void beforeEach(){
        memberRepository.deleteAll();
    }

    @Test
    public void findByUsernameTest(){
        // given
        Member member = createMember();
        memberRepository.save(member);
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findByUsername(member.getUsername()).get();

        // then
        assertThat(findMember.getUsername()).isEqualTo("username");
    }

    @Test
    public void findOneWithRolesByUsernameTest(){

        // given
        Member member = new Member("user", "password", "phoneNum", createAddress(),
                List.of(roleRepository.findByRoleType(RoleType.USER).orElseThrow(RoleNotFoundException::new)), List.of());

        memberRepository.save(member);
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findOneWithRolesByUsername(member.getUsername()).get();

        // then
    }
}
