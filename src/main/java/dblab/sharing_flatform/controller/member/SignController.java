package dblab.sharing_flatform.controller.member;

import dblab.sharing_flatform.dto.member.login.LogInResponseDto;
import dblab.sharing_flatform.dto.member.login.LoginRequestDto;
import dblab.sharing_flatform.dto.member.crud.create.MemberCreateRequestDto;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.service.member.SignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Api(value = "Sign Controller", tags = "Sign")
@Slf4j
@RestController
@RequiredArgsConstructor
public class SignController {
    private final SignService signService;

    @ApiOperation(value = "회원가입", notes = "회원가입을 한다.") // 2
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public Response signup(@Valid @RequestBody MemberCreateRequestDto memberCreateRequestDto){
        signService.signUp(memberCreateRequestDto);
        return Response.success();
    }

    @ApiOperation(value = "로그인", notes = "로그인을 한다.") // 2
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Response login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        LogInResponseDto logInResponseDto = signService.login(loginRequestDto);
        return Response.success(logInResponseDto);
    }
}
