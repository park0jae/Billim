package dblab.sharing_flatform.controller.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
@Controller
public class ChatRoomController {


    @GetMapping("/chat/{username}")
    public String chat(@PathVariable String username, Model model){
        model.addAttribute("username", username);
        return "/chat";
    }
}
