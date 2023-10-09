package dblab.sharing_platform.controller.report;

import dblab.sharing_platform.config.security.util.SecurityUtil;
import dblab.sharing_platform.dto.report.ReportCreateRequest;
import dblab.sharing_platform.dto.report.ReportPagingCondition;
import dblab.sharing_platform.service.report.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static dblab.sharing_platform.config.security.util.SecurityUtil.getCurrentUsernameCheck;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Api(value = "Report Controller", tags = "Report")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @ApiOperation(value = "Report 전체 조회 (ADMIN) ", notes = "ADMIN 권한으로 Report를 조회합니다.")
    @GetMapping
    public ResponseEntity readAllReportByCondForAdmin(@Valid ReportPagingCondition condition) {
        return new ResponseEntity(reportService.readAllReportByCond(condition), OK);
    }

    @ApiOperation(value = "내가 생성한 Report 전체 조회", notes = "내가 생성한 Report를 모두 조회합니다.")
    @GetMapping("/myPage")
    public ResponseEntity readAllMyReport(@Valid ReportPagingCondition condition) {
        condition.setWriterUsername(SecurityUtil.getCurrentUsernameCheck());
        return new ResponseEntity(reportService.readAllMyReport(condition), OK);
    }

    @ApiOperation(value = "게시글 또는 회원에 대한 Report를 생성", notes = "현재 로그인한 유저 정보로 게시글/회원에 대한 Report를 생성합니다.")
    @PostMapping
    public ResponseEntity createReportToPostOrUser(@Valid @RequestBody ReportCreateRequest request) {
        reportService.createReportToPostOrUser(request, getCurrentUsernameCheck());
        return new ResponseEntity(CREATED);
    }

    @ApiOperation(value = "Report ID로 Report를 삭제합니다.", notes = "현재 로그인한 유저 정보로 게시글/회원에 대한 Report를 생성합니다.")
    @DeleteMapping("/{reportId}")
    public ResponseEntity deleteReportByReportId(@ApiParam(name = "삭제할 Report의 ID", required = true) @PathVariable Long reportId) {
        reportService.deleteReportByReportId(reportId);
        return new ResponseEntity(OK);
    }
}
