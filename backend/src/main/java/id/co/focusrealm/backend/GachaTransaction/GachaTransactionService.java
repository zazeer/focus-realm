package id.co.focusrealm.backend.GachaTransaction;

import id.co.focusrealm.backend.Common.Utility;
import id.co.focusrealm.backend.PurchaseTransaction.PurchaseTransactionModel;
import id.co.focusrealm.backend.PurchaseTransaction.PurchaseTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GachaTransactionService {

    @Autowired
    private GachaTransactionRepository gachaTransactionRepository;

    public String insertGachaTransaction(String log_id, String gacha_type, int coins_used){
        GachaTransactionModel gachaTransactionModel = new GachaTransactionModel();

        try {

            gachaTransactionModel.setGacha_transaction_id(generateGachaTransactionId());
            gachaTransactionModel.setGacha_type(gacha_type);
            gachaTransactionModel.setCoins_used(coins_used);
            gachaTransactionModel.setLog_id(log_id);

            gachaTransactionRepository.insertGachaTransaction(gachaTransactionModel);

        } catch (Exception e) {
            log.info("Error at GachaTransactionService insertGachaTransaction", e);
            throw new RuntimeException(e);
        }

        return gachaTransactionModel.getGacha_transaction_id();

    }

    public String generateGachaTransactionId(){
        String newPurchaseTransactionId = null;

        try {

            if(gachaTransactionRepository.checkHasValue() == false){
                newPurchaseTransactionId = "PT001";
            } else {
                String lastLogId = gachaTransactionRepository.getLastGachaTransactionId();
                newPurchaseTransactionId = Utility.generateNextId(lastLogId);
            }

        } catch (Exception e) {
            log.error("Error at GachaTransactionService generateGachaTransactionId", e);
            throw new RuntimeException(e);
        }

        return newPurchaseTransactionId;
    }

}
