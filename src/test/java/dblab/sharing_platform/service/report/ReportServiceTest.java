package dblab.sharing_platform.service.report;

import dblab.sharing_platform.domain.embedded.report_type.ReportType;
import dblab.sharing_platform.domain.member.Member;
import dblab.sharing_platform.domain.post.Post;
import dblab.sharing_platform.domain.report.Report;
import dblab.sharing_platform.dto.report.PagedReportListDto;
import dblab.sharing_platform.dto.report.ReportCreateRequestDto;
import dblab.sharing_platform.dto.report.ReportDto;
import dblab.sharing_platform.dto.report.ReportPagingCondition;
import dblab.sharing_platform.factory.member.MemberFactory;
import dblab.sharing_platform.factory.report.ReportFactory;
import dblab.sharing_platform.repository.member.MemberRepository;
import dblab.sharing_platform.repository.post.PostRepository;
import dblab.sharing_platform.repository.report.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dblab.sharing_platform.factory.post.PostFactory.createPost;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {
    @InjectMocks
    private ReportService reportService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ReportRepository reportRepository;

    Post post;
    Member reporter;
    Member reported;

    @BeforeEach
    public void beforeEach(){
        post = createPost();
        reported = post.getMember();
        reporter = MemberFactory.createMember("reporter");
    }

    @Test
    @DisplayName("게시글 신고 생성")
    public void createPostReportTest() throws Exception {
        //given
        ReflectionTestUtils.setField(post, "id", 1L);
        ReportCreateRequestDto requestDto = new ReportCreateRequestDto(ReportType.POST_REPORT, String.valueOf(post.getId()), "content");
        given(postRepository.findById(post.getId())).willReturn(Optional.ofNullable(post));
        given(memberRepository.findByUsername(reporter.getUsername())).willReturn(Optional.ofNullable(reporter));

        //when
        reportService.createReportToPostOrUser(requestDto, reporter.getUsername());

        //then
        verify(reportRepository).save(any(Report.class));
    }

    @Test
    @DisplayName("버그 신고 생성")
    public void createMemberReportTest() throws Exception {
        //given
        ReportCreateRequestDto requestDto = new ReportCreateRequestDto( ReportType.BUG, null, "content");
        given(memberRepository.findByUsername(reporter.getUsername())).willReturn(Optional.ofNullable(reporter));

        //when
        reportService.createReportToPostOrUser(requestDto, reporter.getUsername());

        //then
        verify(reportRepository).save(any(Report.class));
    }


    @Test
    @DisplayName("신고 삭제")
    public void deleteReportTest() {
        // given
        Report report = ReportFactory.createBugReport(reporter);
        given(reportRepository.findById(report.getId())).willReturn(Optional.of(report));

        // when
        reportService.deleteReportByReportId(report.getId());

        // then
        assertThat(reportRepository.count()).isZero();
    }

    @Test
    @DisplayName("신고 전체 조회")
    public void readAllReportTest() throws Exception {
        //given
        List<Report> reports = new ArrayList<>();
        reports.add(ReportFactory.createBugReport(reporter));
        reports.add(ReportFactory.createPostReport(reporter, post));

        ReportPagingCondition cond = new ReportPagingCondition(0, 10, null, null, null, null);

        given(reportRepository.findAllByCond(cond)).willReturn(new PageImpl<>(List.of(ReportDto.toDto(reports.get(0)), ReportDto.toDto(reports.get(1))), PageRequest.of(cond.getPage(), cond.getSize()), 1));

        //when
        PagedReportListDto result = reportService.readAllReportByCond(cond);

        //then
        assertThat(result.getReportList().get(0).getReportType()).isEqualTo(ReportType.BUG);
        assertThat(result.getReportList().get(1).getReportType()).isEqualTo(ReportType.POST_REPORT);
    }

}