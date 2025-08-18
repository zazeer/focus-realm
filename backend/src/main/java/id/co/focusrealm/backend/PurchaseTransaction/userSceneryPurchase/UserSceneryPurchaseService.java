package id.co.focusrealm.backend.PurchaseTransaction.userSceneryPurchase;

import id.co.focusrealm.backend.PurchaseTransaction.PurchaseTransactionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserSceneryPurchaseService {

    @Autowired
    private UserSceneryPurchaseRepository userSceneryPurchaseRepository;

    public void insertUserSceneryPurchase(String purchase_transaction_id, String user_scenery_id){
        UserSceneryPurchaseModel userSceneryPurchaseModel = new UserSceneryPurchaseModel();

        try {

            userSceneryPurchaseModel.setPurchase_transaction_id(purchase_transaction_id);
            userSceneryPurchaseModel.setUser_scenery_id(user_scenery_id);

            userSceneryPurchaseRepository.insertUserSceneryPurchase(userSceneryPurchaseModel);

        } catch (Exception e) {
            log.info("Error at userSceneryPurchaseRepository insertUserSceneryPurchase", e);
            throw new RuntimeException(e);
        }
    }

}
