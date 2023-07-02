package dblab.sharing_flatform.controller.member;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

// 시큐리티 적용 테스트 컨트롤러
@ApiIgnore
@RestController
public class AuthTestController {

    @GetMapping("/adminPage")
    public String adminPage() {
        return "adminPage";
    }

    @GetMapping("/managerPage")
    public String managerPage() {
        return "managerPage";
    }

    @GetMapping("/userPage")
    public String userPage() {
        return "userPage";
    }

    @GetMapping("/authenticate")
    public String authenticatePage() {
        return "authenticatePage";
    }
}
