package id.co.focusrealm.backend.FocusSession;

import id.co.focusrealm.backend.User.UserModel;
import id.co.focusrealm.backend.User.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping(path = "/focus_session")
public class FocusSessionController {

    @Autowired
    private FocusSessionService focusSessionService;

    @PostMapping("/insert_focus_session")
    public @ResponseBody FocusSessionResponse insertFocusSession(@RequestBody FocusSessionModel focusSessionModel) {
        try {
            return focusSessionService.insertFocusSession(focusSessionModel);
        } catch (Exception e) {
            log.error("Error At FocusSessionController InsertFocusSession");
            throw new RuntimeException(e);
        }
    }

}
