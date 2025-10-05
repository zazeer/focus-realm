package id.co.focusrealm.backend.PurchaseTransaction;

import id.co.focusrealm.backend.Log.LogModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class PurchaseTransactionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertPurchaseTransaction(PurchaseTransactionModel purchaseTransactionModel){

        String sql = """
                    INSERT INTO purchasetransaction
                    (purchase_transaction_id, log_id, purchase_type, coins_used)
                    VALUES (?, ?, ?, ?)
                """;

        try {

            jdbcTemplate.update(sql,
                    purchaseTransactionModel.getPurchase_transaction_id(),
                    purchaseTransactionModel.getLog_id(),
                    purchaseTransactionModel.getPurchase_type(),
                    purchaseTransactionModel.getCoins_used()
            );

        } catch (Exception e) {
            log.info("Error At logRepository insertLog");
            throw new RuntimeException(e);
        }

    }

    public String getLastPurchaseTransactionId(){
        String lastLogId = null;

        try {

            String sql = """
                    SELECT purchase_transaction_id FROM purchasetransaction ORDER BY purchase_transaction_id DESC LIMIT 1;
                    """;

            lastLogId = jdbcTemplate.queryForObject(sql, String.class);

        } catch (Exception e) {
            log.error("Error At purchaseTransaction getLastPurchaseTransactionId");
            throw new RuntimeException(e);
        }

        return lastLogId;
    }

    public boolean checkHasValue(){
        boolean check = false;

        try {

            String checkHasValueSql = """
                    SELECT COUNT(*) FROM purchasetransaction
            """;

            int hasValue =  jdbcTemplate.queryForObject(checkHasValueSql, Integer.class);

            if(hasValue > 0){
                check = true;
            } else {
                check = false;
            }

        } catch (Exception e) {
            log.error("Error At PurchaseTransactionRepository checkHasValue");
            throw new RuntimeException(e);
        }

        return check;
    }

}
