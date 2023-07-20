package dblab.sharing_flatform.controller.member;

import dblab.sharing_flatform.dto.member.MemberPrivateDto;
import dblab.sharing_flatform.dto.member.MemberProfileDto;
import dblab.sharing_flatform.dto.member.crud.read.request.MemberPagingCondition;
import dblab.sharing_flatform.dto.member.crud.update.MemberUpdateRequestDto;
import dblab.sharing_flatform.dto.member.crud.update.OAuthMemberUpdateRequestDto;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.service.member.MemberService;
import dblab.sharing_flatform.config.security.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Member Controller", tags = "Member")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "전체 회원 조회", notes = "전체 회원을 조회합니다. (username 검색)")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid MemberPagingCondition cond) {
        return Response.success(memberService.readAll(cond));
    }

    @ApiOperation(value = "나의 개인 정보 조회", notes = "현재 로그인한 회원 정보를 조회합니다.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response currentUser() {
        String username = SecurityUtil.getCurrentUsername().orElseThrow(AccessDeniedException::new);
        MemberPrivateDto currentMember = memberService.readMyInfo(username);
        return Response.success(currentMember);
    }

    @ApiOperation(value = "회원 프로필 정보 조회", notes = "회원의 프로필 정보를 조회합니다.")
    @GetMapping("/profile/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Response findMemberProfile(@ApiParam(name = "검색할 사용자 아이디", required = true) @PathVariable String username) {
        MemberProfileDto memberInfo = memberService.readMemberProfile(username);
        return Response.success(memberInfo);
    }

    @ApiOperation(value = "회원 삭제", notes = "관리자 또는 본인인 경우 사용자를 삭제한다.")
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteMember(@ApiParam(name = "삭제할 사용자의 username", required = true) @PathVariable String username) {
        memberService.delete(username);
        return Response.success();
    }

    @ApiOperation(value = "회원 정보 수정", notes = "본인인 경우 정보를 수정한다.")
    @PatchMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Response updateMember(@ApiParam(name = "수정할 회원의 username", required = true) @PathVariable String username,
                                 @Valid @ModelAttribute MemberUpdateRequestDto memberUpdateRequestDto) {
        MemberPrivateDto updateMember = memberService.update(username, memberUpdateRequestDto);
        return Response.success(updateMember);
    }

    @ApiOperation(value = "OAuth 회원 추가 정보 등록(필수) / 수정 ", notes = "OAuth2 유저 최초 로그인 시 OAuth 회원 본인의 정보를 등록합니다. / OAuth2 회원의 정보를 수정합니다.")
    @PatchMapping("/update/oauth")
    @ResponseStatus(HttpStatus.OK)
    public Response updateOAuthMember(@Valid @ModelAttribute OAuthMemberUpdateRequestDto requestDto) {
        String username = SecurityUtil.getCurrentUsername().orElseThrow(AccessDeniedException::new);
        MemberPrivateDto updateMember = memberService.oauthMemberUpdate(username, requestDto);
        return Response.success(updateMember);
    }
}
