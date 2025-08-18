package id.co.focusrealm.backend.PurchaseTransaction.userCharacterPurchase;

import id.co.focusrealm.backend.PurchaseTransaction.userSceneryPurchase.UserSceneryPurchaseModel;
import id.co.focusrealm.backend.PurchaseTransaction.userSceneryPurchase.UserSceneryPurchaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserCharacterPurchaseService {

    @Autowired
    private UserCharacterPurchaseRepository userCharacterPurchaseRepository;

    public void insertUserCharacterPurchase(String purchase_transaction_id, String user_scenery_id){
        UserCharacterPurchaseModel userCharacterPurchaseModel = new UserCharacterPurchaseModel();

        try {

            userCharacterPurchaseModel.setPurchase_transaction_id(purchase_transaction_id);
            userCharacterPurchaseModel.setUser_character_id(user_scenery_id);

            userCharacterPurchaseRepository.insertUserCharacterPurchase(userCharacterPurchaseModel);

        } catch (Exception e) {
            log.info("Error at UserCharacterPurchaseService insertUserCharacterPurchase", e);
            throw new RuntimeException(e);
        }
    }

}
