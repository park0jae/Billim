package dblab.sharing_flatform.controller.chat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.SecureRandom;
import java.util.Random;

@Controller
public class ChatController {
    @GetMapping("/chat/{username}")
    public String chat(@PathVariable String username, Model model){
        model.addAttribute("username", username);
        return "/chat";
    }

}
