package dblab.sharing_flatform.service.report;

import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.report.Report;
import dblab.sharing_flatform.dto.report.ReportDto;
import dblab.sharing_flatform.exception.report.ReportNotFoundException;
import dblab.sharing_flatform.dto.report.create.ReportCreateRequestDto;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import dblab.sharing_flatform.repository.report.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public void create(ReportCreateRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId()).orElse(null);
        reportRepository.save(new Report(
                requestDto.getReportType(), requestDto.getContent(),
                memberRepository.findByUsername(requestDto.getReporterName()).orElseThrow(MemberNotFoundException::new),
                post,
                post == null ? null : post.getMember()));
    }

    @Transactional
    public void delete(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(ReportNotFoundException::new);
        reportRepository.delete(report);
    }

    public List<ReportDto> readAll() {
        return ReportDto.toDtoList(reportRepository.findAll());
    }

}
