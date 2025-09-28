package id.co.focusrealm.backend.GachaPage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("/gacha_page")
public class GachaController {

    @Autowired
    private GachaService gachaService;

    @PostMapping("/fetch_gatcha_page_by_user_id")
    public @ResponseBody GachaPageResponse fetchGachaPageByUserId(@RequestBody GachaModel gachaModel) {
        try {
            return gachaService.fetchGachaPageByUserId(gachaModel);
        } catch (Exception e) {
            log.error("Error At GachaController fetchGachaPageByUserId");
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/do_gacha_character")
    public @ResponseBody GachaPageResponse doGachaCharacter(@RequestBody GachaModel gachaModel) {
        try {
            return gachaService.doGachaCharacter(gachaModel);
        } catch (Exception e) {
            log.error("Error At GachaController doGachaCharacter");
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/do_gacha_scenery")
    public @ResponseBody GachaPageResponse doGachaScenery(@RequestBody GachaModel gachaModel) {
        try {
            return gachaService.doGachaScenery(gachaModel);
        } catch (Exception e) {
            log.error("Error At GachaController doGachaScenery");
            throw new RuntimeException(e);
        }
    }

}
