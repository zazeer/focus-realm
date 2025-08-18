package id.co.focusrealm.backend.GachaPage;

import id.co.focusrealm.backend.ShopPage.ShopPageModel;
import id.co.focusrealm.backend.ShopPage.ShopPageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GachaService {

    @Autowired
    private GachaRepository gachaRepository;

    private double commonChance = 69.9;
    private double rareChance = 29.9;
    private double legendaryChance = 0.2;

    public GachaPageResponse fetchGachaPageByUserId(GachaModel gachaModel){
        GachaPageResponse gachaPageResponse = new GachaPageResponse();

        try {
            gachaRepository.fetchUserDataGachaPage(gachaModel);

            gachaPageResponse.setErrorCode("200");
            gachaPageResponse.setErrorMessage("Success");

        } catch (Exception e) {
            gachaPageResponse.setErrorCode("500");
            gachaPageResponse.setErrorMessage("Error");
            log.error("Error At GachaService fetchGachaPageByUserId");
            throw new RuntimeException(e);
        }

        return gachaPageResponse;
    }

    public GachaPageResponse gachaCharacter(GachaModel gachaModel){
        GachaPageResponse gachaPageResponse = new GachaPageResponse();

        try {
            gachaRepository.fetchUserDataGachaPage(gachaModel);

            for(int i = 0; i < gachaModel.getGacha_amount(); i++){



            }


        } catch (Exception e) {
            gachaPageResponse.setErrorCode("500");
            gachaPageResponse.setErrorMessage("Error");
            log.error("Error At GachaService gachaCharacter");
            throw new RuntimeException(e);
        }

        return gachaPageResponse;

    }

}
