package id.co.focusrealm.backend.ShopPage;

import id.co.focusrealm.backend.GalleryPage.GalleryPageModel;
import id.co.focusrealm.backend.GalleryPage.GalleryPageResponse;
import id.co.focusrealm.backend.GalleryPage.GalleryPageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("/shop_page")
public class ShopPageController {

    @Autowired
    private ShopPageService shopPageService;

    @PostMapping("/fetch_shop_page_by_user_id")
    public @ResponseBody ShopPageResponse fetchShopPageByUserId(@RequestBody ShopPageModel shopPageModel) {
        try {
            return shopPageService.fetchShopPageByUserId(shopPageModel);
        } catch (Exception e) {
            log.error("Error At ShopPageController fetchShopPageByUserId");
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/purchase_character")
    public @ResponseBody ShopPageResponse purchaseCharacter(@RequestBody ShopPageModel shopPageModel) {
        try {
            return shopPageService.purchaseCharacter(shopPageModel);
        } catch (Exception e) {
            log.error("Error At ShopPageController fetchShopPageByUserId");
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/purchase_scenery")
    public @ResponseBody ShopPageResponse purchaseScenery(@RequestBody ShopPageModel shopPageModel) {
        try {
            return shopPageService.purchaseScenery(shopPageModel);
        } catch (Exception e) {
            log.error("Error At ShopPageController fetchShopPageByUserId");
            throw new RuntimeException(e);
        }
    }

}
