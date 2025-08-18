package id.co.focusrealm.backend.PurchaseTransaction;

import id.co.focusrealm.backend.Common.Utility;
import id.co.focusrealm.backend.Log.LogModel;
import id.co.focusrealm.backend.Log.LogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class PurchaseTransactionService {

    @Autowired
    private PurchaseTransactionRepository purchaseTransactionRepository;

    public String insertPurchaseTransaction(String log_id, String purchase_type, int coins_used){
        PurchaseTransactionModel purchaseTransactionModel = new PurchaseTransactionModel();

        try {

            purchaseTransactionModel.setPurchase_transaction_id(generatePurchaseTransactionId());
            purchaseTransactionModel.setPurchase_type(purchase_type);
            purchaseTransactionModel.setCoins_used(coins_used);
            purchaseTransactionModel.setLog_id(log_id);

            purchaseTransactionRepository.insertPurchaseTransaction(purchaseTransactionModel);

        } catch (Exception e) {
            log.info("Error at PurchaseTransactionService purchaseTransactionModel", e);
            throw new RuntimeException(e);
        }

        return purchaseTransactionModel.getPurchase_transaction_id();

    }

    public String generatePurchaseTransactionId(){
        String newPurchaseTransactionId = null;

        try {

            if(purchaseTransactionRepository.checkHasValue() == false){
                newPurchaseTransactionId = "PT001";
            } else {
                String lastLogId = purchaseTransactionRepository.getLastPurchaseTransactionId();
                newPurchaseTransactionId = Utility.generateNextId(lastLogId);
            }

        } catch (Exception e) {
            log.error("Error at PurchaseTransactionService generatePurchaseTransactionId", e);
            throw new RuntimeException(e);
        }

        return newPurchaseTransactionId;
    }

}
