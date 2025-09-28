package id.co.focusrealm.backend.GachaTransaction.userCharacterGacha;

import id.co.focusrealm.backend.PurchaseTransaction.userCharacterPurchase.UserCharacterPurchaseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserCharacterGachaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertUserCharacterGacha(UserCharacterGachaModel userCharacterGachaModel){

        String sql = """
                INSERT INTO user_character_gacha
                (gacha_transaction_id, user_character_id)
                VALUES (?, ?)
                """;

        try {

            jdbcTemplate.update(sql,
                    userCharacterGachaModel.getGacha_transaction_id(),
                    userCharacterGachaModel.getUser_character_id()
            );

        } catch (Exception e) {
            log.info("Error At UserCharacterGachaRepository insertUserCharacterPurchase");
            throw new RuntimeException(e);
        }

    }

}
