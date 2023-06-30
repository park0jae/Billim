package dblab.sharing_flatform.controller.member;

import dblab.sharing_flatform.dto.member.LogInResponseDto;
import dblab.sharing_flatform.dto.member.LoginRequestDto;
import dblab.sharing_flatform.dto.member.SignUpRequestDto;
import dblab.sharing_flatform.service.SignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final SignService signService;
    
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody SignUpRequestDto signUpRequestDto){
        signService.signUp(signUpRequestDto);

        return (ResponseEntity) ResponseEntity.ok();
    }

    @PostMapping("/login")
    public LogInResponseDto login(@RequestBody LoginRequestDto loginRequestDto){
        LogInResponseDto loginObject = signService.login(loginRequestDto);

        return loginObject;
    }
}
