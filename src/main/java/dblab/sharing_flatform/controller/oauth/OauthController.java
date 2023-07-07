package dblab.sharing_flatform.controller.oauth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OauthController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
