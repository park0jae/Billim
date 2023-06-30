package dblab.sharing_flatform.controller.member;

import dblab.sharing_flatform.dto.member.LogInResponseDto;
import dblab.sharing_flatform.dto.member.LoginRequestDto;
import dblab.sharing_flatform.dto.member.SignUpRequestDto;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.service.SignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SignController {
    private final SignService signService;
    @PostMapping("/sign-up")
    public Response signup(@Valid @RequestBody SignUpRequestDto signUpRequestDto){
        signService.signUp(signUpRequestDto);
        return Response.success();
    }

    @PostMapping("/login")
    public Response login(@RequestBody LoginRequestDto loginRequestDto){
        LogInResponseDto logInResponseDto = signService.login(loginRequestDto);
        return Response.success(logInResponseDto);
    }
}
