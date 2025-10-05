package id.co.focusrealm.backend.GachaPage;

import id.co.focusrealm.backend.CharacterSceneryModel.CharacterModel;
import id.co.focusrealm.backend.CharacterSceneryModel.SceneryModel;
import id.co.focusrealm.backend.CharacterSceneryModelPackage.ObtainedCharacterModel;
import id.co.focusrealm.backend.CharacterSceneryModelPackage.UnobtainedCharacterModel;
import id.co.focusrealm.backend.GachaPage.HistoryModels.CharacterHistoryModel;
import id.co.focusrealm.backend.GachaPage.HistoryModels.SceneryHistoryModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@Slf4j
public class GachaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    RowMapper<GachaModel> gatchaPageRowMapper = ((rs, rowNum) -> {
        GachaModel gachaModel = new GachaModel();

        // User Information For HomePage
        gachaModel.setUser_pity(rs.getInt("pity"));
        gachaModel.setUser_coins(rs.getInt("coins"));

        return gachaModel;
    });

    public GachaModel fetchUserDataGachaPage(GachaModel gachaModel){

        String sql = """
                SELECT coins, pity FROM "User" WHERE user_id = ?
                """;

        try {

            GachaModel result = jdbcTemplate.queryForObject(sql, gatchaPageRowMapper, gachaModel.getUser_id());
            return result;

        } catch (Exception e) {
            log.error("Error At GachaRepository fetchUserDataGachaPage");
            throw new RuntimeException(e);
        }
    }

    public CharacterModel fetchCharacterData(String characterId){

        String sql = """
                SELECT * FROM character WHERE character_id = ?
                """;

        try {
            CharacterModel result = jdbcTemplate.queryForObject(sql, new Object[]{characterId},
                            (rs, rowNum) -> {
                                CharacterModel temp = new CharacterModel();
                                temp.setCharacter_id(rs.getString("character_id"));
                                temp.setCharacter_name(rs.getString("character_name"));
                                temp.setCharacter_rarity(rs.getString("character_rarity"));
                                temp.setPrice(rs.getInt("price"));
                                temp.setRelease_date(rs.getDate("release_date"));
                                temp.setFile_name(rs.getString("file_name"));
                                temp.setCharacter_description(rs.getString("character_description"));
                                return temp;
                            });
            return result;

        } catch (Exception e) {
            log.error("Error At GachaRepository fetchCharacterData");
            throw new RuntimeException(e);
        }
    }

    public SceneryModel fetchSceneryData(String sceneryId){

        String sql = """
                SELECT * FROM scenery WHERE scenery_id = ?
                """;

        try {
            SceneryModel result = jdbcTemplate.queryForObject(sql, new Object[]{sceneryId},
                    (rs, rowNum) -> {
                        SceneryModel temp = new SceneryModel();
                        temp.setScenery_id(rs.getString("scenery_id"));
                        temp.setScenery_name(rs.getString("scenery_name"));
                        temp.setScenery_rarity(rs.getString("scenery_rarity"));
                        temp.setPrice(rs.getInt("price"));
                        temp.setFile_name(rs.getString("file_name"));
                        temp.setRelease_date(rs.getDate("release_date"));
                        temp.setScenery_description(rs.getString("scenery_description"));
                        return temp;
                    });
            return result;

        } catch (Exception e) {
            log.error("Error At GachaRepository fetchSceneryData");
            throw new RuntimeException(e);
        }
    }

    public ArrayList<UnobtainedCharacterModel> getUnobtainedCharacter(String user_id) {

        String sql = "SELECT c.character_id, c.character_name, c.character_rarity, c.price, c.release_date, c.file_name, c.character_description\n" +
                "FROM character c \n" +
                "LEFT JOIN usercharacter uc ON c.character_id = uc.character_id AND uc.user_id = ?\n" +
                "WHERE uc.character_id IS NULL";
        ArrayList<UnobtainedCharacterModel> characterModel = new ArrayList<UnobtainedCharacterModel>();

        try {
            jdbcTemplate.query(sql, new Object[]{user_id},(rs, rowNum) -> {
                UnobtainedCharacterModel character = new UnobtainedCharacterModel();
                character.setCharacter_id(rs.getString("character_id"));
                character.setCharacter_name(rs.getString("character_name"));
                character.setCharacter_rarity(rs.getString("character_rarity"));
                character.setCharacter_description(rs.getString("character_description"));
                character.setPrice(rs.getInt("price"));
                character.setFile_name(rs.getString("file_name"));
                character.setRelease_date(rs.getDate("release_date"));
                characterModel.add(character);
                return null;
            });

        } catch (Exception e) {
            log.error("Error at GachaRepository getUnobtainedCharacter", e);
            throw new RuntimeException(e);
        }

        return characterModel;
    }

    public ArrayList<ObtainedCharacterModel> getObtainedCharacter(String user_id) {

        String sql = "SELECT c.character_id, c.character_name, c.character_rarity, c.price, c.release_date, c.file_name, c.character_description, uc.acquire_date, uc.chosen_character\n" +
                "FROM character c \n" +
                "JOIN usercharacter uc ON c.character_id = uc.character_id\n" +
                "WHERE user_id = ?";
        ArrayList<ObtainedCharacterModel> characterModel = new ArrayList<ObtainedCharacterModel>();

        try {
            jdbcTemplate.query(sql, new Object[]{user_id},(rs, rowNum) -> {
                ObtainedCharacterModel character = new ObtainedCharacterModel();
                character.setCharacter_id(rs.getString("character_id"));
                character.setCharacter_name(rs.getString("character_name"));
                character.setCharacter_rarity(rs.getString("character_rarity"));
                character.setCharacter_description(rs.getString("character_description"));
                character.setPrice(rs.getInt("price"));
                character.setFile_name(rs.getString("file_name"));
                character.setRelease_date(rs.getDate("release_date"));
                character.setAcquire_date(rs.getDate("acquire_date"));
                character.setChosen_character(rs.getBoolean("chosen_character"));
                characterModel.add(character);
                return null;
            });

        } catch (Exception e) {
            log.error("Error at GachaRepository getObtainedCharacter", e);
            throw new RuntimeException(e);
        }

        return characterModel;
    }

    public ArrayList<CharacterModel> getAllCharacter(){

        String sql = """
                SELECT * FROM character
                """;

        ArrayList<CharacterModel> allCharacter = new ArrayList<>();

        try {
            jdbcTemplate.query(sql, new Object[]{},(rs, rowNum) -> {
                CharacterModel characterModel = new CharacterModel();
                characterModel.setCharacter_id(rs.getString("character_id"));
                characterModel.setCharacter_name(rs.getString("character_name"));
                characterModel.setCharacter_rarity(rs.getString("character_rarity"));
                characterModel.setCharacter_description(rs.getString("character_description"));
                characterModel.setPrice(rs.getInt("price"));
                characterModel.setFile_name(rs.getString("file_name"));
                characterModel.setRelease_date(rs.getDate("release_date"));
                allCharacter.add(characterModel);
                return null;
            });


        } catch (Exception e) {
            log.info("Error At GachaRepository getAllCharacter");
            throw new RuntimeException(e);
        }

        return allCharacter;
    }

    public ArrayList<SceneryModel> getAllScenery(){

        String sql = """
                SELECT * FROM scenery
                """;

        ArrayList<SceneryModel> allScenery = new ArrayList<>();

        try {
            jdbcTemplate.query(sql, new Object[]{},(rs, rowNum) -> {
                SceneryModel sceneryModel = new SceneryModel();
                sceneryModel.setScenery_id(rs.getString("scenery_id"));
                sceneryModel.setScenery_name(rs.getString("scenery_name"));
                sceneryModel.setScenery_rarity(rs.getString("scenery_rarity"));
                sceneryModel.setScenery_description(rs.getString("scenery_description"));
                sceneryModel.setPrice(rs.getInt("price"));
                sceneryModel.setFile_name(rs.getString("file_name"));
                sceneryModel.setRelease_date(rs.getDate("release_date"));
                allScenery.add(sceneryModel);
                return null;
            });


        } catch (Exception e) {
            log.info("Error At GachaRepository getAllScenery");
            throw new RuntimeException(e);
        }

        return allScenery;
    }

    public boolean checkCharacterExists(ArrayList<CharacterModel> characters, int index, String user_id){

        try {

            String characterId = characters.get(index).getCharacter_id();
            String checkCharacterSql = "SELECT COUNT(*) \n" +
                    "FROM usercharacter\n" +
                    "WHERE user_id = ? AND character_id = ?";
            int check = jdbcTemplate.queryForObject(checkCharacterSql, Integer.class, user_id, characterId);

            if(check == 0){
                return false;
            } else if (check > 0){
                return true;
            }

        } catch (DataAccessException e) {
            log.error("Error At GachaRepository checkCharacterExists");
            throw new RuntimeException(e);
        }

        return false;
    }

    public boolean checkCharacterExists(String character_id, String user_id){

        try {

            String checkCharacterSql = "SELECT COUNT(*) \n" +
                    "FROM usercharacter\n" +
                    "WHERE user_id = ? AND character_id = ?";
            int check = jdbcTemplate.queryForObject(checkCharacterSql, Integer.class, user_id, character_id);

            if(check == 0){
                return false;
            } else if (check > 0){
                return true;
            }

        } catch (DataAccessException e) {
            log.error("Error At GachaRepository checkCharacterExists");
            throw new RuntimeException(e);
        }

        return false;
    }

    public boolean checkSceneryExists(ArrayList<SceneryModel> scenery, int index, String user_id){

        try {

            String sceneryId = scenery.get(index).getScenery_id();
            String checkScenerySql = """
                    SELECT COUNT(*)
                    FROM userscenery
                    WHERE user_id = ? AND scenery_id = ?
                    """;
            int check = jdbcTemplate.queryForObject(checkScenerySql, Integer.class, user_id, sceneryId);

            if(check == 0){
                return false;
            } else if (check > 0){
                return true;
            }

        } catch (DataAccessException e) {
            log.error("Error At GachaRepository checkSceneryExists");
            throw new RuntimeException(e);
        }

        return false;
    }

    public boolean checkSceneryExists(String scenery_id, String user_id){

        try {

            String checkScenerySql = """
                    SELECT COUNT(*)
                    FROM userscenery
                    WHERE user_id = ? AND scenery_id = ?
                    """;
            int check = jdbcTemplate.queryForObject(checkScenerySql, Integer.class, user_id, scenery_id);

            if(check == 0){
                return false;
            } else if (check > 0){
                return true;
            }

        } catch (DataAccessException e) {
            log.error("Error At GachaRepository checkCharacterExists");
            throw new RuntimeException(e);
        }

        return false;
    }

    public String getUserCharacterId(ArrayList<CharacterModel> characters, int index, String user_id) {
        try {
            String userCharacterId;

            String characterId = characters.get(index).getCharacter_id();
            String checkCharacterSql = "SELECT user_character_id \n" +
                    "FROM usercharacter\n" +
                    "WHERE user_id = ? and character_id = ?";
            userCharacterId = jdbcTemplate.queryForObject(checkCharacterSql, String.class, user_id, characterId);

            return userCharacterId;

        } catch (DataAccessException e) {
            log.error("Error At GachaRepository getUserCharacterId");
            throw new RuntimeException(e);
        }
    }

    public String getUserCharacterId(String character_id, String user_id) {
        try {
            String userCharacterId;

            String checkCharacterSql = "SELECT user_character_id \n" +
                    "FROM usercharacter\n" +
                    "WHERE user_id = ? and character_id = ?";
            userCharacterId = jdbcTemplate.queryForObject(checkCharacterSql, String.class, user_id, character_id);

            return userCharacterId;

        } catch (DataAccessException e) {
            log.error("Error At GachaRepository getUserCharacterId");
            throw new RuntimeException(e);
        }
    }

    public String getUserSceneryId(ArrayList<SceneryModel> scenery, int index, String user_id) {
        try {
            String userCharacterId;

            String characterId = scenery.get(index).getScenery_id();
            String checkCharacterSql = """
                    SELECT USER_SCENERY_ID
                    FROM USERSCENERY
                    WHERE user_id = ? AND scenery_id = ?
                    """;
            userCharacterId = jdbcTemplate.queryForObject(checkCharacterSql, String.class, user_id, characterId);

            return userCharacterId;

        } catch (DataAccessException e) {
            log.error("Error At GachaRepository getUserCharacterId");
            throw new RuntimeException(e);
        }
    }

    public String getUserSceneryId(String scenery_id, String user_id) {
        try {
            String userCharacterId;

            String checkCharacterSql = """
                    SELECT USER_SCENERY_ID
                    FROM USERSCENERY
                    WHERE user_id = ? AND scenery_id = ?
                    """;
            userCharacterId = jdbcTemplate.queryForObject(checkCharacterSql, String.class, user_id, scenery_id);

            return userCharacterId;

        } catch (DataAccessException e) {
            log.error("Error At GachaRepository getUserSceneryId");
            throw new RuntimeException(e);
        }
    }

    public void increaseUserCoins(String user_id, int amount){
        try {

            String updateUserCoinSql = """
                    UPDATE "User"
                    SET coins = coins + ?
                    WHERE user_id = ?
                    """;

            jdbcTemplate.update(updateUserCoinSql, amount, user_id);

        } catch (Exception e) {
            log.error("Error at GachaTransactionRepository updateUserCoins", e);
            throw new RuntimeException(e);
        }
    }

    public void decreaseUserCoins(String user_id, int amount){
        try {

            String updateUserCoinSql = """
                    UPDATE "User"
                    SET coins = coins - ?
                    WHERE user_id = ?
                    """;

            jdbcTemplate.update(updateUserCoinSql, amount, user_id);

        } catch (Exception e) {
            log.error("Error at GachaTransactionRepository decreaseUserCoins", e);
            throw new RuntimeException(e);
        }
    }

    public int getUserPity(String user_id){

        try {

            String getUserPitySql = """
                    SELECT pity FROM "User" WHERE user_id = ?
                    """;

            int userPity = jdbcTemplate.queryForObject(getUserPitySql, Integer.class, user_id);

            return userPity;

        } catch (Exception e) {
            log.error("Error at GachaTransactionRepository getUserPity", e);
            throw new RuntimeException(e);
        }
    }

    public void updateUserPity(String user_id, int pity){
        try {

            String updateUserPitySql = """
                        UPDATE "User"
                        SET pity = ?
                        WHERE user_id = ?
                    """;

            jdbcTemplate.update(updateUserPitySql, pity, user_id);

        } catch (Exception e) {
            log.error("Error at GachaTransactionRepository updateUserPity", e);
            throw new RuntimeException(e);
        }
    }

    public ArrayList<CharacterHistoryModel> getCharacterHistory(String user_id) {

        String sql = """
                SELECT c.character_id, c.character_name, c.character_rarity, l.created_at\s
                FROM log l 	
                JOIN gachatransaction gt
                ON l.log_id = gt.log_id
                JOIN user_character_gacha ucg
                ON gt.gacha_transaction_id = ucg.gacha_transaction_id
                JOIN usercharacter uc
                ON ucg.user_character_id = uc.user_character_id
                JOIN character c
                ON uc.character_id = c.character_id
                WHERE l.user_id = ? AND gt.gacha_type = ?
                """;
        ArrayList<CharacterHistoryModel> characterHistoryModels = new ArrayList<CharacterHistoryModel>();

        try {
            jdbcTemplate.query(sql, new Object[]{user_id, "Character"},(rs, rowNum) -> {
                CharacterHistoryModel character = new CharacterHistoryModel();
                character.setCharacter_id(rs.getString("character_id"));
                character.setCharacter_name(rs.getString("character_name"));
                character.setCharacter_rarity(rs.getString("character_rarity"));
                character.setObtainedDate(rs.getDate("created_at"));
                characterHistoryModels.add(character);
                return null;
            });

        } catch (Exception e) {
            log.error("Error at GachaRepository getCharacterHistory", e);
            throw new RuntimeException(e);
        }

        return characterHistoryModels;
    }

    public ArrayList<SceneryHistoryModel> getSceneryHistory(String user_id) {

        String sql = """
                    SELECT s.scenery_id, s.scenery_name, s.scenery_rarity, l.created_at
                    FROM log l 	
                    JOIN gachatransaction gt
                    ON l.log_id = gt.log_id
                    JOIN user_scenery_gacha usg
                    ON gt.gacha_transaction_id = usg.gacha_transaction_id
                    JOIN userscenery us
                    ON usg.user_scenery_id = us.user_scenery_id
                    JOIN scenery s
                    ON us.scenery_id = s.scenery_id
                    WHERE l.user_id = ? AND gt.gacha_type = ?
                """;
        ArrayList<SceneryHistoryModel> sceneryHistoryModels = new ArrayList<SceneryHistoryModel>();

        try {
            jdbcTemplate.query(sql, new Object[]{user_id, "Scenery"},(rs, rowNum) -> {
                SceneryHistoryModel scenery = new SceneryHistoryModel();
                scenery.setScenery_id(rs.getString("scenery_id"));
                scenery.setScenery_name(rs.getString("scenery_name"));
                scenery.setScenery_rarity(rs.getString("scenery_rarity"));
                scenery.setObtainedDate(rs.getDate("created_at"));
                sceneryHistoryModels.add(scenery);
                return null;
            });

        } catch (Exception e) {
            log.error("Error at GachaRepository getSceneryHistory", e);
            throw new RuntimeException(e);
        }

        return sceneryHistoryModels;
    }

    public int getUserCoin(String user_id){

        int userCoin = 0;

        try {
            String userCoinSql = """
                    SELECT coins FROM "User" WHERE user_id = ?
                    """;

            userCoin = jdbcTemplate.queryForObject(userCoinSql, Integer.class, user_id);

        } catch (Exception e) {
            log.error("Error at FocusSessionRepository getUserCoin", e);
            throw new RuntimeException(e);
        }

        return userCoin;
    }

}
