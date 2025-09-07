package id.co.focusrealm.backend.CustomizationPage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("/customization_page")
public class CustomizationPageController {

    @Autowired
    private CustomizationPageService customizationPageService;

    @PostMapping("/fetch_customization_page_by_user_id")
    public @ResponseBody CustomizationPageResponse fetchCustomizationPageById(@RequestBody CustomizationPageModel customizationPageModel) {
        try {
            return customizationPageService.fetchCustomizationPageById(customizationPageModel);
        } catch (Exception e) {
            log.error("Error At CustomizationPageController fetchCustomizationPageById");
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/update_user_settings")
    public @ResponseBody CustomizationPageResponse updateUserSettings(@RequestBody CustomizationPageModel customizationPageModel) {
        try {
            return customizationPageService.updateUserSettings(customizationPageModel);
        } catch (Exception e) {
            log.error("Error At CustomizationPageController updateUserSettings");
            throw new RuntimeException(e);
        }
    }

}
