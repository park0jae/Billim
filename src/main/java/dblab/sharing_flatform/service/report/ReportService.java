package dblab.sharing_flatform.service.report;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.report.Report;
import dblab.sharing_flatform.dto.report.PagedReportListDto;
import dblab.sharing_flatform.dto.report.ReportCreateRequestDto;
import dblab.sharing_flatform.dto.report.ReportPagingCondition;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.post.PostNotFoundException;
import dblab.sharing_flatform.exception.report.ReportNotFoundException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import dblab.sharing_flatform.repository.report.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public void createReportToPostOrUser(ReportCreateRequestDto requestDto, String username) {
        Post post = requestDto.getPostId() == null ? null : postRepository.findById(requestDto.getPostId()).orElseThrow(PostNotFoundException::new);
        Member member = memberRepository.findByUsername(username).orElseThrow(MemberNotFoundException::new);
        reportRepository.save(new Report(requestDto.getReportType(),
                requestDto.getContent(),
                member,
                post,
                post == null ? null : post.getMember()));
    }
    @Transactional
    public void deleteReportByReportId(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(ReportNotFoundException::new);
        reportRepository.delete(report);
    }
    public PagedReportListDto readAllReportByCond(ReportPagingCondition cond) {
        return PagedReportListDto.toDto(reportRepository.findAllByCond(cond));
    }
}
