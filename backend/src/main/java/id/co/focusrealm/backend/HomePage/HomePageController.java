package id.co.focusrealm.backend.HomePage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping(path = "/home_page")
public class HomePageController {

    @Autowired
    private HomePageService homePageService;

    @PostMapping("/fetch_home_page_data_by_user_id")
    public @ResponseBody HomePageResponse fetchHomePageDataByUserId(@RequestBody HomePageModel homePageModel) {
        try {
            log.info("Entering HomePage, fetchHomePageDataByUserId");
            return homePageService.fetchHomePageDataByUserId(homePageModel);
        } catch (Exception e) {
            log.error("Error At HomePageController fetchHomePageDataByUserId");
            throw new RuntimeException(e);
        }
    }

}
