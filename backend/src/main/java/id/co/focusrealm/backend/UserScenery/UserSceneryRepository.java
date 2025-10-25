package id.co.focusrealm.backend.UserScenery;

import id.co.focusrealm.backend.UserCharacter.UserCharacterModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class UserSceneryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertUserScenery(UserSceneryModel userSceneryModel){

        try {
            String insertUserCharacterSql = """
                    INSERT INTO "userscenery" (
                    	user_scenery_id, user_id, scenery_id, acquire_date, chosen_scenery
                    ) VALUES (?, ?, ?, ?, ?)
                    """;

            jdbcTemplate.update(insertUserCharacterSql,
                    userSceneryModel.getUser_scenery_id(),
                    userSceneryModel.getUser_id(),
                    userSceneryModel.getScenery_id(),
                    userSceneryModel.getAcquire_date(),
                    userSceneryModel.isChosen_scenery()
            );

        } catch (Exception e) {
            log.info("Error in UserSceneryRepository insertUserScenery");
            throw new RuntimeException(e);
        }

    }

    public boolean checkHasValue(){
        boolean check = false;

        try {

            String checkHasValueSql = """
                    SELECT COUNT(*) FROM userscenery
            """;

            int hasValue =  jdbcTemplate.queryForObject(checkHasValueSql, Integer.class);

            if(hasValue > 0){
                check = true;
            } else {
                check = false;
            }

        } catch (Exception e) {
            log.error("Error At UserSceneryRepository checkHasValue");
            throw new RuntimeException(e);
        }

        return check;
    }

    public String getLastUserSceneryId(){
        String lastSceneryId = null;

        try {

            String getLastUserIdSql = """
                        SELECT user_scenery_id FROM userscenery ORDER BY user_scenery_id DESC LIMIT 1;
                    """;

            lastSceneryId = jdbcTemplate.queryForObject(getLastUserIdSql, String.class);

        } catch (Exception e) {
            log.error("Error At UserSceneryRepository getLastUserSceneryId");
            throw new RuntimeException(e);
        }

        return lastSceneryId;
    }

}
