package id.co.focusrealm.backend.PurchaseTransaction;

import lombok.Data;

@Data
public class PurchaseTransactionModel {

    private String purchase_transaction_id;
    private String log_id;
    private String purchase_type;
    private int coins_used;

}
