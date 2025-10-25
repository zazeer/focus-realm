package id.co.focusrealm.backend.PurchaseTransaction.userCharacterPurchase;

import id.co.focusrealm.backend.PurchaseTransaction.userSceneryPurchase.UserSceneryPurchaseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserCharacterPurchaseRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertUserCharacterPurchase(UserCharacterPurchaseModel userCharacterPurchaseModel){

        String sql = """
                INSERT INTO user_character_purchase
                (purchase_transaction_id, user_character_id)
                VALUES (?, ?)
                """;

        try {

            jdbcTemplate.update(sql,
                    userCharacterPurchaseModel.getPurchase_transaction_id(),
                    userCharacterPurchaseModel.getUser_character_id()
            );

        } catch (Exception e) {
            log.info("Error At UserCharacterPurchaseRepository insertUserCharacterPurchase");
            throw new RuntimeException(e);
        }

    }

}
