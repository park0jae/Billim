package dblab.sharing_flatform.controller.member;

import dblab.sharing_flatform.dto.member.MemberPrivateDto;
import dblab.sharing_flatform.dto.member.MemberProfileDto;
import dblab.sharing_flatform.dto.member.crud.update.MemberUpdateRequestDto;
import dblab.sharing_flatform.dto.member.crud.update.OAuthMemberUpdateRequestDto;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.service.member.MemberService;
import dblab.sharing_flatform.config.security.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Api(value = "Member Controller", tags = "Member")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "현재 사용자 조회", notes = "현재 사용자를 조회한다.")
    @GetMapping
    public Response currentUser(){
        String currentUsername = SecurityUtil.getCurrentUsername().get();
        MemberPrivateDto currentMember = memberService.findMyInfo(currentUsername);
        return Response.success(currentMember);
    }

    @ApiOperation(value = "회원 프로필 정보 조회", notes = "회원의 프로필 정보를 조회합니다.")
    @GetMapping("/profile")
    public Response findMemberProfile(@RequestParam String username){
        MemberProfileDto memberInfo = memberService.findMemberProfile(username);
        return Response.success(memberInfo);
    }

    @ApiOperation(value = "특정 사용자 조회", notes = "ADMIN 권한이 있는 경우 특정 사용자를 조회한다.")
    @GetMapping("/{id}")
    public Response findMemberByAdmin(@ApiParam(name="조회할 사용자 아이디" , required = true) @PathVariable Long id){
        MemberPrivateDto memberInfo = memberService.findMemberByIdOnlyAdmin(id);
        return Response.success(memberInfo);
    }

    @ApiOperation(value = "사용자 삭제", notes = "관리자 또는 본인인 경우 사용자를 삭제한다.")
    @DeleteMapping("/{id}")
    public Response deleteMember(@ApiParam(name = "삭제할 사용자 아이디", required = true) @PathVariable Long id){
        memberService.delete(id);
        return Response.success();
    }

    @ApiOperation(value = "회원 정보 수정", notes = "본인인 경우 정보를 수정한다.")
    @PatchMapping("/{id}")
    public Response updateMember(@ApiParam(name = "수정할 사용자 아이디", required = true) @PathVariable Long id,
                                 @Valid @ModelAttribute MemberUpdateRequestDto memberUpdateRequestDto){
        MemberPrivateDto updateMember = memberService.update(id, memberUpdateRequestDto);
        return Response.success(updateMember);
    }

    @ApiOperation(value = "OAuth 회원 정보 등록", notes = "OAuth 회원 본인의 정보를 등록합니다.")
    @PatchMapping("/oauth")
    public Response updateOAuthMember(@Valid @ModelAttribute OAuthMemberUpdateRequestDto requestDto){
        String currentUserId = SecurityUtil.getCurrentUserId().orElseThrow(AccessDeniedException::new);
        System.out.println("currentUserId = " + currentUserId);

        MemberPrivateDto updateMember = memberService.oauthMemberUpdate(Long.valueOf(currentUserId), requestDto);
        return Response.success(updateMember);
    }

}
