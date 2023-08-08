package dblab.sharing_platform.repository.report;

import dblab.sharing_platform.config.querydsl.QuerydslConfig;
import dblab.sharing_platform.domain.category.Category;
import dblab.sharing_platform.domain.member.Member;
import dblab.sharing_platform.domain.post.Post;
import dblab.sharing_platform.domain.report.Report;
import dblab.sharing_platform.repository.category.CategoryRepository;
import dblab.sharing_platform.repository.member.MemberRepository;
import dblab.sharing_platform.repository.post.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static dblab.sharing_platform.factory.member.MemberFactory.createMember;
import static dblab.sharing_platform.factory.post.PostFactory.createPost;
import static dblab.sharing_platform.factory.report.ReportFactory.createBugReport;
import static dblab.sharing_platform.factory.report.ReportFactory.createPostReport;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class ReportRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ReportRepository reportRepository;

    @PersistenceContext
    EntityManager em;

    Post post;
    Category category;
    Member postMember;


    public void postInit() {
        post = createPost();
        category = post.getCategory();
        postMember = post.getMember();

        memberRepository.save(postMember);
        clear();

        categoryRepository.save(category);
        clear();

        postRepository.save(post);
        clear();
    }

    @Test
    @DisplayName("게시글 신고 생성")
    public void createPostReportTest() throws Exception {
        //given
        postInit();

        Member reporter = memberRepository.save(createMember("reporter"));
        clear();

        //when
        Report report = reportRepository.save(createPostReport(reporter, post));
        clear();

        //then
        assertThat(reportRepository.count()).isEqualTo(1);
        assertThat(reportRepository.findById(report.getId())).isPresent();
        assertThat(reportRepository.findById(report.getId()).get().getPost().getMember()).isNotNull();
    }

    @Test
    @DisplayName("버그 신고 생성")
    public void createMemberReportTest() throws Exception {
        // given
        Member reporter = memberRepository.save(createMember("reporter"));
        clear();

        // when
        Report report = reportRepository.save(createBugReport(reporter));
        clear();

        //then
        assertThat(reportRepository.count()).isEqualTo(1);
        assertThat(reportRepository.findById(report.getId())).isPresent();
        assertThat(reportRepository.findById(report.getId()).get().getPost()).isNull();
        assertThat(reportRepository.findById(report.getId()).get().getReportType()).isEqualTo(report.getReportType());
    }

    @Test
    @DisplayName("신고 작성자 삭제 -> 신고 삭제")
    public void deleteCascadeReporter() throws Exception {
        // given
        postInit();
        Member reporter = memberRepository.save(createMember("reporter"));
        Report report = reportRepository.save(createPostReport(reporter, post));
        clear();

        // when
        memberRepository.delete(reporter);
        clear();

        // then
        assertThat(reportRepository.count()).isZero();
    }


    public void clear() {
        em.flush();
        em.clear();
    }

}