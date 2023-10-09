package dblab.sharing_platform.controller.member;

import dblab.sharing_platform.dto.member.MemberPagingCondition;
import dblab.sharing_platform.dto.member.MemberUpdateRequestDto;
import dblab.sharing_platform.dto.member.OAuthMemberUpdateRequestDto;
import dblab.sharing_platform.dto.response.Response;
import dblab.sharing_platform.service.member.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static dblab.sharing_platform.config.security.util.SecurityUtil.getCurrentUsernameCheck;
import static org.springframework.http.HttpStatus.OK;

@Api(value = "Member Controller", tags = "Member")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "전체 회원 조회", notes = "전체 회원을 조회합니다. (username 검색)")
    @GetMapping
    @ResponseStatus(OK)
    public Response readAllMemberByCond(@Valid MemberPagingCondition cond) {
        return Response.success(OK.value(), memberService.readAllMemberByCond(cond));
    }

    @ApiOperation(value = "나의 개인 정보 조회", notes = "현재 로그인한 회원 정보를 조회합니다.")
    @GetMapping("/my-profile")
    @ResponseStatus(OK)
    public Response readInfoCurrentUser() {
        return Response.success(OK.value(),memberService.readCurrentUserInfoByUsername(getCurrentUsernameCheck()));
    }

    @ApiOperation(value = "회원 프로필 정보 조회", notes = "회원의 프로필 정보를 조회합니다.")
    @GetMapping("/{username}")
    @ResponseStatus(OK)
    public Response findMemberProfileByUsername(@ApiParam(name = "검색할 사용자 아이디", required = true) @PathVariable String username) {
        return Response.success(OK.value(),memberService.readMemberProfileByNickname(username));
    }

    @ApiOperation(value = "회원 삭제", notes = "관리자 또는 본인인 경우 사용자를 삭제한다.")
    @DeleteMapping
    @ResponseStatus(OK)
    public Response deleteMemberByUsername() {
        memberService.deleteMemberByUsername(getCurrentUsernameCheck());
        return Response.success(OK.value());
    }

    @ApiOperation(value = "자신의 회원 정보 수정", notes = "본인인 경우 정보를 수정한다.")
    @PatchMapping
    @ResponseStatus(OK)
    public Response updateMemberInfo(@Valid @ModelAttribute MemberUpdateRequestDto memberUpdateRequestDto) {
        return Response.success(OK.value(),memberService.updateMember(getCurrentUsernameCheck(), memberUpdateRequestDto));
    }

    @ApiOperation(value = "OAuth 회원 추가 정보 등록(필수) / 수정 ", notes = "OAuth2 유저 최초 로그인 시 OAuth 회원 본인의 정보를 등록합니다. / OAuth2 회원의 정보를 수정합니다.")
    @PatchMapping("/oauth")
    @ResponseStatus(OK)
    public Response updateOAuthMemberInfo(@Valid @ModelAttribute OAuthMemberUpdateRequestDto requestDto) {
        return Response.success(OK.value(), memberService.oauthMemberUpdate(getCurrentUsernameCheck(), requestDto));
    }
}
