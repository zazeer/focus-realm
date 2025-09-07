package id.co.focusrealm.backend.TimerPage;

import id.co.focusrealm.backend.TimerPage.Models.TimerPageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping(path = "/timer_page")
public class TimerPageController {

    @Autowired
    private TimerPageService timerPageService;

    @PostMapping("/fetch_timer_page_data_by_user_id")
    public @ResponseBody TimerPageResponse fetchTimerPageDataByUserId(@RequestBody TimerPageModel timerPageModel) {
        try {
            return timerPageService.fetchTimerPageDataByUserId(timerPageModel);
        } catch (Exception e) {
            log.error("Error At TimerPageController fetchTimerPageDataByUserId");
            throw new RuntimeException(e);
        }
    }

}
