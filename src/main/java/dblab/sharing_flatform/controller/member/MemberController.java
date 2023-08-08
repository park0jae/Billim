package dblab.sharing_flatform.controller.member;

import dblab.sharing_flatform.dto.member.MemberPagingCondition;
import dblab.sharing_flatform.dto.member.MemberUpdateRequestDto;
import dblab.sharing_flatform.dto.member.OAuthMemberUpdateRequestDto;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.service.member.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static dblab.sharing_flatform.config.security.util.SecurityUtil.getCurrentUsernameCheck;

@Api(value = "Member Controller", tags = "Member")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "전체 회원 조회", notes = "전체 회원을 조회합니다. (username 검색)")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response readAllMemberByCond(@Valid MemberPagingCondition cond) {
        return Response.success(memberService.readAllMemberByCond(cond));
    }

    @ApiOperation(value = "나의 개인 정보 조회", notes = "현재 로그인한 회원 정보를 조회합니다.")
    @GetMapping("/my-profile")
    @ResponseStatus(HttpStatus.OK)
    public Response readInfoCurrentUser() {
        return Response.success(memberService.readCurrentUserInfoByUsername(getCurrentUsernameCheck()));
    }

    @ApiOperation(value = "회원 프로필 정보 조회", notes = "회원의 프로필 정보를 조회합니다.")
    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Response findMemberProfileByUsername(@ApiParam(name = "검색할 사용자 아이디", required = true) @PathVariable String username) {
        return Response.success(memberService.readMemberProfileByNickname(username));
    }

    @ApiOperation(value = "회원 삭제", notes = "관리자 또는 본인인 경우 사용자를 삭제한다.")
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Response deleteMemberByUsername() {
        memberService.deleteMemberByUsername(getCurrentUsernameCheck());
        return Response.success();
    }

    @ApiOperation(value = "자신의 회원 정보 수정", notes = "본인인 경우 정보를 수정한다.")
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public Response updateMemberInfo(@Valid @ModelAttribute MemberUpdateRequestDto memberUpdateRequestDto) {
        return Response.success(memberService.updateMember(getCurrentUsernameCheck(), memberUpdateRequestDto));
    }

    @ApiOperation(value = "OAuth 회원 추가 정보 등록(필수) / 수정 ", notes = "OAuth2 유저 최초 로그인 시 OAuth 회원 본인의 정보를 등록합니다. / OAuth2 회원의 정보를 수정합니다.")
    @PatchMapping("/oauth")
    @ResponseStatus(HttpStatus.OK)
    public Response updateOAuthMemberInfo(@Valid @ModelAttribute OAuthMemberUpdateRequestDto requestDto) {
        return Response.success(memberService.oauthMemberUpdate(getCurrentUsernameCheck(), requestDto));
    }
}
