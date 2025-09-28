package id.co.focusrealm.backend.GachaTransaction;

import lombok.Data;

@Data
public class GachaTransactionModel {

    private String gacha_transaction_id;
    private String log_id;
    private String gacha_type;
    private int coins_used;

}
