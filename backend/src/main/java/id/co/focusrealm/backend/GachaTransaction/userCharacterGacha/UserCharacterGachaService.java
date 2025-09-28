package id.co.focusrealm.backend.GachaTransaction.userCharacterGacha;

import id.co.focusrealm.backend.PurchaseTransaction.userCharacterPurchase.UserCharacterPurchaseModel;
import id.co.focusrealm.backend.PurchaseTransaction.userCharacterPurchase.UserCharacterPurchaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserCharacterGachaService {

    @Autowired
    private UserCharacterGachaRepository userCharacterGachaRepository;

    public void insertUserCharacterGacha(String gacha_transaction_id, String user_character_id){
        UserCharacterGachaModel userCharacterGachaModel = new UserCharacterGachaModel();

        try {

            userCharacterGachaModel.setGacha_transaction_id(gacha_transaction_id);
            userCharacterGachaModel.setUser_character_id(user_character_id);

            userCharacterGachaRepository.insertUserCharacterGacha(userCharacterGachaModel);

        } catch (Exception e) {
            log.info("Error at UserCharacterPurchaseService insertUserCharacterGacha", e);
            throw new RuntimeException(e);
        }
    }

}
