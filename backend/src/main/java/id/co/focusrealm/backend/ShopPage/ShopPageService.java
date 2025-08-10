package id.co.focusrealm.backend.ShopPage;

import id.co.focusrealm.backend.GalleryPage.GalleryPageResponse;
import id.co.focusrealm.backend.UserCharacter.UserCharacterModel;
import id.co.focusrealm.backend.UserCharacter.UserCharacterRepository;
import id.co.focusrealm.backend.UserCharacter.UserCharacterService;
import id.co.focusrealm.backend.UserScenery.UserSceneryModel;
import id.co.focusrealm.backend.UserScenery.UserSceneryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
@Slf4j
public class ShopPageService {

    @Autowired
    private ShopPageRepository shopPageRepository;

    @Autowired
    private UserCharacterService userCharacterService;

    @Autowired
    private UserSceneryService userSceneryService;

    public ShopPageResponse fetchShopPageByUserId(ShopPageModel shopPageModel){
        ShopPageResponse shopPageResponse = new ShopPageResponse();

        try {

            shopPageRepository.fetchShopPageByUserId(shopPageModel);

            shopPageResponse.setShopPageModel(shopPageModel);
            shopPageResponse.setErrorCode("200");
            shopPageResponse.setErrorMessage("Success");

        } catch (Exception e) {
            shopPageResponse.setErrorCode("500");
            shopPageResponse.setErrorMessage("Error");
            log.error("Error At ShopPageService fetchShopPageByUserId");
            throw new RuntimeException(e);
        }

        return shopPageResponse;
    }

    public ShopPageResponse purchaseCharacter(ShopPageModel shopPageModel){
        ShopPageResponse shopPageResponse = new ShopPageResponse();

        try {
            if(shopPageRepository.checkUserHaveTheCharacter(shopPageModel) == true){
                shopPageRepository.fetchShopPageByUserId(shopPageModel);
                shopPageResponse.setShopPageModel(shopPageModel);
                shopPageResponse.setErrorCode("400");
                shopPageResponse.setErrorMessage("User Already Have The Character");
                return shopPageResponse;
            }

            if(shopPageRepository.checkUserCoinSufficientCharacter(shopPageModel) == true){
                shopPageRepository.updateUserRemainingCoinCharacter(shopPageModel);

                Date created_at = (new Timestamp(System.currentTimeMillis()));
                // Setting User Character Data
                UserCharacterModel userCharacterModel = new UserCharacterModel();
                userCharacterModel.setUser_id(shopPageModel.getUser_id());
                userCharacterModel.setCharacter_id(shopPageModel.getCharacter_id());
                userCharacterModel.setAcquire_date(created_at);
                userCharacterModel.setChosen_character(false);
                userCharacterService.insertUserCharacter(userCharacterModel);

                shopPageRepository.fetchShopPageByUserId(shopPageModel);
                shopPageResponse.setShopPageModel(shopPageModel);
                shopPageResponse.setErrorCode("200");
                shopPageResponse.setErrorMessage("Successfully Purchased the character");

            } else {
                shopPageRepository.fetchShopPageByUserId(shopPageModel);
                shopPageResponse.setShopPageModel(shopPageModel);
                shopPageResponse.setErrorCode("400");
                shopPageResponse.setErrorMessage("Insufficient Coins");
                return shopPageResponse;
            }

        } catch (Exception e) {
            shopPageResponse.setErrorCode("500");
            shopPageResponse.setErrorMessage("Error");
            log.error("Error At ShopPageService ");
            throw new RuntimeException(e);
        }

        return shopPageResponse;
    }

    public ShopPageResponse purchaseScenery(ShopPageModel shopPageModel){
        ShopPageResponse shopPageResponse = new ShopPageResponse();

        try {
            if(shopPageRepository.checkUserHaveTheScenery(shopPageModel) == true){
                shopPageRepository.fetchShopPageByUserId(shopPageModel);
                shopPageResponse.setShopPageModel(shopPageModel);
                shopPageResponse.setErrorCode("400");
                shopPageResponse.setErrorMessage("User Already Have The Scenery");
                return shopPageResponse;
            }

            if(shopPageRepository.checkUserCoinSufficientScenery(shopPageModel) == true){
                shopPageRepository.updateUserRemainingCoinScenery(shopPageModel);

                Date created_at = (new Timestamp(System.currentTimeMillis()));
                // Setting User Scenery Data
                UserSceneryModel userSceneryModel = new UserSceneryModel();
                userSceneryModel.setUser_id(shopPageModel.getUser_id());
                userSceneryModel.setScenery_id(shopPageModel.getScenery_id());
                userSceneryModel.setAcquire_date(created_at);
                userSceneryModel.setChosen_scenery(false);
                userSceneryService.insertUserScenery(userSceneryModel);

                shopPageRepository.fetchShopPageByUserId(shopPageModel);
                shopPageResponse.setShopPageModel(shopPageModel);
                shopPageResponse.setErrorCode("200");
                shopPageResponse.setErrorMessage("Successfully Purchased the Scenery");

            } else {
                shopPageRepository.fetchShopPageByUserId(shopPageModel);
                shopPageResponse.setShopPageModel(shopPageModel);
                shopPageResponse.setErrorCode("400");
                shopPageResponse.setErrorMessage("Insufficient Coins");
                return shopPageResponse;
            }

        } catch (Exception e) {
            shopPageResponse.setErrorCode("500");
            shopPageResponse.setErrorMessage("Error");
            log.error("Error At ShopPageService ");
            throw new RuntimeException(e);
        }

        return shopPageResponse;
    }

}
