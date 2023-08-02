package dblab.sharing_flatform.service.report;

import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.report.Report;
import dblab.sharing_flatform.dto.report.PagedReportListDto;
import dblab.sharing_flatform.dto.report.ReportDto;
import dblab.sharing_flatform.dto.report.ReportPagingCondition;
import dblab.sharing_flatform.exception.post.PostNotFoundException;
import dblab.sharing_flatform.exception.report.ReportNotFoundException;
import dblab.sharing_flatform.dto.report.create.ReportCreateRequestDto;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import dblab.sharing_flatform.repository.report.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public void create(ReportCreateRequestDto requestDto, String username) {
        Post post = requestDto.getPostId() == null ? null : postRepository.findById(requestDto.getPostId()).orElseThrow(PostNotFoundException::new);
        reportRepository.save(new Report(requestDto.getReportType(),
                requestDto.getContent(),
                memberRepository.findByUsername(username).orElseThrow(MemberNotFoundException::new),
                post,
                post == null ? null : post.getMember()));
    }

    @Transactional
    public void delete(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(ReportNotFoundException::new);
        reportRepository.delete(report);
    }

    public PagedReportListDto readAll(ReportPagingCondition cond) {
        return PagedReportListDto.toDto(reportRepository.findAllByCond(cond));
    }

}
