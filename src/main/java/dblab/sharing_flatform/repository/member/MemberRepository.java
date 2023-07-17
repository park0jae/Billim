package dblab.sharing_flatform.repository.member;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.dto.member.MemberDto;
import dblab.sharing_flatform.dto.member.crud.read.response.PagedMemberListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, QMemberRepository{

    Optional<Member> findByUsername(String username);

    @EntityGraph(attributePaths = "roles")
    Optional<Member> findOneWithRolesByUsername(String username);

    boolean existsByUsername(String username);

    Optional<Member> findByProviderAndUsername(String provider, String username);

}
