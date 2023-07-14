package dblab.sharing_flatform;

import dblab.sharing_flatform.domain.address.Address;
import dblab.sharing_flatform.domain.category.Category;
import dblab.sharing_flatform.domain.comment.Comment;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.role.Role;
import dblab.sharing_flatform.domain.role.RoleType;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.post.PostNotFoundException;
import dblab.sharing_flatform.exception.role.RoleNotFoundException;
import dblab.sharing_flatform.repository.category.CategoryRepository;
import dblab.sharing_flatform.repository.comment.CommentRepository;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import dblab.sharing_flatform.repository.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final PasswordEncoder passwordEncoder;


    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void initDB() {
        initRoles();
        initAdmin();
        initManager();
        initMember();
        initCategory();
        initPost();
        initComment();
    }

    @Transactional
    public void initRoles() {
        RoleType[] values = RoleType.values();
        List<Role> roles = Arrays.stream(values).map(roleType -> new Role(roleType)).collect(Collectors.toList());
        roleRepository.saveAll(roles);
    }

    @Transactional
    public void initAdmin() {
        memberRepository.save(new Member(
                "admin", passwordEncoder.encode("1q2w3e4r@@"),
                "011", new Address("jeonju", "d1", "s1", "z1"),
                "None",
                List.of(roleRepository.findByRoleType(RoleType.ADMIN).orElseThrow(RoleNotFoundException::new),
                        roleRepository.findByRoleType(RoleType.MANAGER).orElseThrow(RoleNotFoundException::new),
                        roleRepository.findByRoleType(RoleType.USER).orElseThrow(RoleNotFoundException::new)
                ),
                List.of()
        ));
    }

    @Transactional
    public void initManager() {
        memberRepository.save(new Member(
                "manager", passwordEncoder.encode("2w2w2w3e!"),
                "012", new Address("jeonju", "d1", "s1", "z1"),
                "None",
                List.of(roleRepository.findByRoleType(RoleType.USER).orElseThrow(RoleNotFoundException::new),
                        roleRepository.findByRoleType(RoleType.MANAGER).orElseThrow(RoleNotFoundException::new)),
                List.of()
        ));
    }

    @Transactional
    public void initMember() {
        memberRepository.save(new Member(
                "member", passwordEncoder.encode("2w2w2w3e!"),
                "013", new Address("jeonju", "d1", "s1", "z1"),
                "None",
                List.of(roleRepository.findByRoleType(RoleType.USER).orElseThrow(RoleNotFoundException::new)),
                List.of()
        ));
    }

    @Transactional
    public void initCategory() {
        Category c1 = categoryRepository.save(new Category("category1", null));
        Category c2 = categoryRepository.save(new Category("category2", c1));
        Category c3 = categoryRepository.save(new Category("category3", c1));
        Category c4 = categoryRepository.save(new Category("category4", c2));
        Category c5 = categoryRepository.save(new Category("category5", c2));
        Category c6 = categoryRepository.save(new Category("category6", c2));
        Category c7 = categoryRepository.save(new Category("category7", c3));
        Category c8 = categoryRepository.save(new Category("category8", null));
    }

    @Transactional
    public void initPost() {
        IntStream.range(0,3).forEach(
                i -> {
                    postRepository.save(new Post(i + "번째 글의 제목", "내용",
                            categoryRepository.findByName("category1").orElse(null),
                            null,
                            List.of(),
                            memberRepository.findByUsername("admin").orElse(null), null));
                }
            );
    }

    @Transactional
    public void initComment() {
        Comment comment1 = new Comment("content1", true,
                postRepository.findById(1L).orElseThrow(PostNotFoundException::new),
                memberRepository.findByUsername("member").orElseThrow(MemberNotFoundException::new), null);
        commentRepository.save(comment1);

        Comment comment2 = new Comment("content1", false,
                postRepository.findById(1L).orElseThrow(PostNotFoundException::new),
                memberRepository.findByUsername("admin").orElseThrow(MemberNotFoundException::new), comment1);
        commentRepository.save(comment2);
    }

}


