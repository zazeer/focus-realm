package id.co.focusrealm.backend.GachaTransaction.userSceneryGacha;

import id.co.focusrealm.backend.GachaTransaction.userCharacterGacha.UserCharacterGachaModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class UserSceneryGachaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertUserSceneryGacha(UserSceneryGachaModel userSceneryGachaModel){

        String sql = """
                INSERT INTO user_scenery_gacha
                (gacha_transaction_id, user_scenery_id)
                VALUES (?, ?)
                """;

        try {

            jdbcTemplate.update(sql,
                    userSceneryGachaModel.getGacha_transaction_id(),
                    userSceneryGachaModel.getUser_scenery_id()
            );

        } catch (Exception e) {
            log.info("Error At UserSceneryGachaRepository insertUserSceneryGacha");
            throw new RuntimeException(e);
        }

    }

}
