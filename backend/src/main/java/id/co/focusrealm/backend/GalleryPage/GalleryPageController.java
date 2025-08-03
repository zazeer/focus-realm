package id.co.focusrealm.backend.GalleryPage;

import id.co.focusrealm.backend.HomePage.HomePageModel;
import id.co.focusrealm.backend.HomePage.HomePageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("/gallery_page")
public class GalleryPageController {

    @Autowired
    private GalleryPageService galleryPageService;

    @PostMapping("/fetch_gallery_page_by_user_id")
    public @ResponseBody GalleryPageResponse fetchGalleryPageByUserId(@RequestBody GalleryPageModel galleryPageModel) {
        try {
            return galleryPageService.fetchGalleryPageByUserId(galleryPageModel);
        } catch (Exception e) {
            log.error("Error At galleryPageController fetchGalleryPageByUserId");
            throw new RuntimeException(e);
        }
    }

}
