package dblab.sharing_flatform.controller.report;

import dblab.sharing_flatform.config.security.util.SecurityUtil;
import dblab.sharing_flatform.dto.report.PagedReportListDto;
import dblab.sharing_flatform.dto.report.ReportPagingCondition;
import dblab.sharing_flatform.dto.report.create.ReportCreateRequestDto;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.service.report.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static dblab.sharing_flatform.config.security.util.SecurityUtil.getCurrentUsernameCheck;

@Api(value = "Report Controller", tags = "Report")
@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    @ApiOperation(value = "Report 전체 조회 (ADMIN) ", notes = "ADMIN 권한으로 Report를 조회합니다.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid ReportPagingCondition cond) {
        return Response.success(reportService.readAll(cond));
    }

    @ApiOperation(value = "게시글 또는 회원에 대한 Report를 생성", notes = "현재 로그인한 유저 정보로 게시글/회원에 대한 Report를 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@Valid @RequestBody ReportCreateRequestDto requestDto) {
        reportService.create(requestDto, getCurrentUsernameCheck());
        return Response.success();
    }

    @ApiOperation(value = "Report ID로 Report를 삭제합니다.", notes = "현재 로그인한 유저 정보로 게시글/회원에 대한 Report를 생성합니다.")
    @DeleteMapping("/{reportId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Response delete(@ApiParam(name = "삭제할 Report의 ID", required = true) @PathVariable Long reportId) {
        reportService.delete(reportId);
        return Response.success();
    }


}
