package id.co.focusrealm.backend.UserCharacter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class UserCharacterRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertUserCharacter(UserCharacterModel userCharacterModel){

        try {

            String insertUserCharacterSql = """
                    INSERT INTO "usercharacter" (
                    	user_character_id, user_id, character_id, acquire_date, chosen_character
                    ) VALUES (?, ?, ?, ?, ?)
                    """;

            jdbcTemplate.update(insertUserCharacterSql,
                    userCharacterModel.getUser_character_id(),
                    userCharacterModel.getUser_id(),
                    userCharacterModel.getCharacter_id(),
                    userCharacterModel.getAcquire_date(),
                    userCharacterModel.isChosen_character()
            );

        } catch (Exception e) {
            log.info("Error in UserCharacterRepository insertUserCharacter");
            throw new RuntimeException(e);
        }

    }

    public boolean checkHasValue(){
        boolean check = false;

        try {

            String checkHasValueSql = """
                    SELECT COUNT(*) FROM usercharacter
            """;

            int hasValue =  jdbcTemplate.queryForObject(checkHasValueSql, Integer.class);

            if(hasValue > 0){
                check = true;
            } else {
                check = false;
            }

        } catch (Exception e) {
            log.error("Error At UserCharacterRepository checkHasValue");
            throw new RuntimeException(e);
        }

        return check;
    }

    public String getLastUserCharacterId(){
        String lastUserCharacterId = null;

        try {

            String getLastUserIdSql = """
                        SELECT user_character_id FROM usercharacter ORDER BY user_character_id DESC LIMIT 1;
                    """;

            lastUserCharacterId = jdbcTemplate.queryForObject(getLastUserIdSql, String.class);

        } catch (Exception e) {
            log.error("Error At UserCharacterRepository getLastUserCharacterId");
            throw new RuntimeException(e);
        }

        return lastUserCharacterId;
    }

}
