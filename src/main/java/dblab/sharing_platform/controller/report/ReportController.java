package dblab.sharing_platform.controller.report;

import dblab.sharing_platform.config.security.util.SecurityUtil;
import dblab.sharing_platform.dto.report.ReportPagingCondition;
import dblab.sharing_platform.dto.report.ReportCreateRequestDto;
import dblab.sharing_platform.dto.response.Response;
import dblab.sharing_platform.service.report.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static dblab.sharing_platform.config.security.util.SecurityUtil.getCurrentUsernameCheck;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Api(value = "Report Controller", tags = "Report")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @ApiOperation(value = "Report 전체 조회 (ADMIN) ", notes = "ADMIN 권한으로 Report를 조회합니다.")
    @GetMapping
    @ResponseStatus(OK)
    public Response readAllReportByCondForAdmin(@Valid ReportPagingCondition cond) {
        return Response.success(OK.value(), reportService.readAllReportByCond(cond));
    }

    @ApiOperation(value = "내가 생성한 Report 전체 조회", notes = "내가 생성한 Report를 모두 조회합니다.")
    @GetMapping("/myPage")
    @ResponseStatus(OK)
    public Response readAllMyReport(@Valid ReportPagingCondition cond) {
        cond.setWriterUsername(SecurityUtil.getCurrentUsernameCheck());
        return Response.success(OK.value(),reportService.readAllMyReport(cond));
    }

    @ApiOperation(value = "게시글 또는 회원에 대한 Report를 생성", notes = "현재 로그인한 유저 정보로 게시글/회원에 대한 Report를 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response createReportToPostOrUser(@Valid @RequestBody ReportCreateRequestDto requestDto) {
        reportService.createReportToPostOrUser(requestDto, getCurrentUsernameCheck());
        return Response.success(CREATED.value());
    }

    @ApiOperation(value = "Report ID로 Report를 삭제합니다.", notes = "현재 로그인한 유저 정보로 게시글/회원에 대한 Report를 생성합니다.")
    @DeleteMapping("/{reportId}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteReportByReportId(@ApiParam(name = "삭제할 Report의 ID", required = true) @PathVariable Long reportId) {
        reportService.deleteReportByReportId(reportId);
        return Response.success(OK.value());
    }

}
