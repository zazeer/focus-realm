package id.co.focusrealm.backend.ProfilePage;

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
@RequestMapping(path = "/profile_page")
public class ProfilePageController {

    @Autowired
    private ProfilePageService profilePageService;

    @PostMapping("/fetch_profile_page_data")
    public @ResponseBody ProfilePageResponse fetchProfilePageData(@RequestBody ProfilePageModel profilePageModel) {
        try {
            return profilePageService.fetchProfilePageData(profilePageModel);
        } catch (Exception e) {
            log.error("Error At ProfilePageController fetchProfilePageData");
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/change_username")
    public @ResponseBody ProfilePageResponse changeUserName(@RequestBody ProfilePageModel profilePageModel) {
        try {
            return profilePageService.changeUserName(profilePageModel);
        } catch (Exception e) {
            log.error("Error At ProfilePageController changeUserName");
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/change_password")
    public @ResponseBody ProfilePageResponse changePassword(@RequestBody ProfilePageModel profilePageModel) {
        try {
            return profilePageService.changePassword(profilePageModel);
        } catch (Exception e) {
            log.error("Error At ProfilePageController changePassword");
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/change_email")
    public @ResponseBody ProfilePageResponse changeEmail(@RequestBody ProfilePageModel profilePageModel) {
        try {
            return profilePageService.changeEmail(profilePageModel);
        } catch (Exception e) {
            log.error("Error At ProfilePageController changeEmail");
            throw new RuntimeException(e);
        }
    }

}
