package id.co.focusrealm.backend.GachaTransaction;

import id.co.focusrealm.backend.FocusSession.FocusSessionModel;
import id.co.focusrealm.backend.PurchaseTransaction.PurchaseTransactionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class GachaTransactionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertGachaTransaction(GachaTransactionModel gachaTransactionModel){

        String sql = """
                    INSERT INTO gachatransaction
                    (gacha_transaction_id, log_id, gacha_type, coins_used)
                    VALUES (?, ?, ?, ?)
                """;

        try {

            jdbcTemplate.update(sql,
                    gachaTransactionModel.getGacha_transaction_id(),
                    gachaTransactionModel.getLog_id(),
                    gachaTransactionModel.getGacha_type(),
                    gachaTransactionModel.getCoins_used()
            );

        } catch (Exception e) {
            log.info("Error At logRepository insertLog");
            throw new RuntimeException(e);
        }

    }

    public String getLastGachaTransactionId(){
        String lastLogId = null;

        try {

            String sql = """
                    SELECT gacha_transaction_id FROM gachatransaction ORDER BY gacha_transaction_id DESC LIMIT 1;
                    """;

            lastLogId = jdbcTemplate.queryForObject(sql, String.class);

        } catch (Exception e) {
            log.error("Error At GachaTransactionRepository getLastGachaTransactionId");
            throw new RuntimeException(e);
        }

        return lastLogId;
    }

    public boolean checkHasValue(){
        boolean check = false;

        try {

            String checkHasValueSql = """
                    SELECT COUNT(*) FROM gachatransaction
            """;

            int hasValue =  jdbcTemplate.queryForObject(checkHasValueSql, Integer.class);

            if(hasValue > 0){
                check = true;
            } else {
                check = false;
            }

        } catch (Exception e) {
            log.error("Error At GachaTransactionRepository checkHasValue");
            throw new RuntimeException(e);
        }

        return check;
    }

}
