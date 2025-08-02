package id.co.focusrealm.backend.GeminiChatbotApi;

import id.co.focusrealm.backend.HomePage.HomePageModel;
import id.co.focusrealm.backend.HomePage.HomePageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/gemini")
public class GeminiChatbotController {

    @Autowired
    private GeminiChatbotService geminiService;

    @PostMapping("/chatbot")
    public @ResponseBody GeminiChatbotResponse geminiChatbot(@RequestBody GeminiChatbotModel model) {
        try {
            return geminiService.userChat(model);
        } catch (Exception e) {
            log.error("Error At geminiChatbotController geminiChatBot");
            throw new RuntimeException(e);
        }
    }

}

