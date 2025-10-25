package id.co.focusrealm.backend.PurchaseTransaction.userSceneryPurchase;

import id.co.focusrealm.backend.PurchaseTransaction.PurchaseTransactionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserSceneryPurchaseRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertUserSceneryPurchase(UserSceneryPurchaseModel userSceneryPurchaseModel){

        String sql = """
                    INSERT INTO user_scenery_purchase
                    (purchase_transaction_id, user_scenery_id)
                    VALUES (?, ?)
                """;

        try {

            jdbcTemplate.update(sql,
                    userSceneryPurchaseModel.getPurchase_transaction_id(),
                    userSceneryPurchaseModel.getUser_scenery_id()
            );

        } catch (Exception e) {
            log.info("Error At UserSceneryPurchaseRepository insertUserSceneryPurchase");
            throw new RuntimeException(e);
        }

    }

}
