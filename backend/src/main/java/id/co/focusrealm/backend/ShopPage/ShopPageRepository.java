package id.co.focusrealm.backend.ShopPage;

import id.co.focusrealm.backend.CharacterSceneryModelPackage.ObtainedCharacterModel;
import id.co.focusrealm.backend.CharacterSceneryModelPackage.ObtainedSceneryModel;
import id.co.focusrealm.backend.CharacterSceneryModelPackage.UnobtainedCharacterModel;
import id.co.focusrealm.backend.CharacterSceneryModelPackage.UnobtainedSceneryModel;
import id.co.focusrealm.backend.UserCharacter.UserCharacterModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Slf4j
@Repository
public class ShopPageRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
            log.error("Error at ShopPageRepository getUnobtainedCharacter", e);
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
            log.error("Error at ShopPageRepository getObtainedCharacter", e);
            throw new RuntimeException(e);
        }

        return characterModel;
    }

    public ArrayList<UnobtainedSceneryModel> getUnobtainedScenery(String user_id) {

        String sql = "SELECT s.scenery_id, s.scenery_name, s.scenery_rarity, s.price, s.file_name, s.release_date, s.scenery_description\n" +
                "FROM scenery s\n" +
                "LEFT JOIN userscenery us ON s.scenery_id = us.scenery_id AND us.user_id = ?\n" +
                "WHERE us.scenery_id IS NULL;\n";
        ArrayList<UnobtainedSceneryModel> unobtainedScenery = new ArrayList<UnobtainedSceneryModel>();

        try {

            jdbcTemplate.query(sql, new Object[]{user_id},(rs, rowNum) -> {
                UnobtainedSceneryModel scenery = new UnobtainedSceneryModel();
                scenery.setScenery_id(rs.getString("scenery_id"));
                scenery.setScenery_name(rs.getString("scenery_name"));
                scenery.setScenery_rarity(rs.getString("scenery_rarity"));
                scenery.setScenery_description(rs.getString("scenery_description"));
                scenery.setPrice(rs.getInt("price"));
                scenery.setFile_name(rs.getString("file_name"));
                scenery.setRelease_date(rs.getDate("release_date"));
                unobtainedScenery.add(scenery);
                return null;
            });

        } catch (Exception e) {
            log.error("Error at ShopPageRepository getUnobtainedScenery", e);
            throw new RuntimeException(e);
        }

        return unobtainedScenery;
    }

    public ArrayList<ObtainedSceneryModel> getObtainedScenery(String user_id) {

        String sql = "SELECT s.scenery_id, s.scenery_name, s.scenery_rarity, s.price, s.file_name, s.release_date, s.scenery_description, us.acquire_date, us.chosen_scenery \n" +
                "FROM scenery s JOIN userscenery us ON us.scenery_id = s.scenery_id\n" +
                "WHERE user_id = ?";
        ArrayList<ObtainedSceneryModel> obtainedScenery = new ArrayList<ObtainedSceneryModel>();

        try {

            jdbcTemplate.query(sql, new Object[]{user_id},(rs, rowNum) -> {
                ObtainedSceneryModel scenery = new ObtainedSceneryModel();
                scenery.setScenery_id(rs.getString("scenery_id"));
                scenery.setScenery_name(rs.getString("scenery_name"));
                scenery.setScenery_rarity(rs.getString("scenery_rarity"));
                scenery.setScenery_description(rs.getString("scenery_description"));
                scenery.setPrice(rs.getInt("price"));
                scenery.setFile_name(rs.getString("file_name"));
                scenery.setRelease_date(rs.getDate("release_date"));
                scenery.setAcquire_date(rs.getDate("acquire_date"));
                scenery.setChosen_scenery(rs.getBoolean("chosen_scenery"));
                obtainedScenery.add(scenery);
                return null;
            });

        } catch (Exception e) {
            log.error("Error at ShopPageRepository getObtainedScenery", e);
            throw new RuntimeException(e);
        }

        return obtainedScenery;
    }

    public int getUserCoin(String user_id){

        int userCoin = 0;

        try {

            String userCoinSql = """
                    SELECT coins FROM "User" WHERE user_id = ?
                    """;

            userCoin = jdbcTemplate.queryForObject(userCoinSql, Integer.class, user_id);

        } catch (Exception e) {
            log.error("Error at ShopPageRepository getUserCoin", e);
            throw new RuntimeException(e);
        }

        return userCoin;
    }

    public int getCharacterPrice(String character_id){

        int characterPrice = 0;

        try {

            String characterCoinRequiredSql = """
                    SELECT price FROM character WHERE character_id = ?
                    """;

            characterPrice = jdbcTemplate.queryForObject(characterCoinRequiredSql, Integer.class, character_id);

        } catch (Exception e) {
            log.error("Error at ShopPageRepository getCharacterPrice", e);
            throw new RuntimeException(e);
        }

        return characterPrice;
    }

    public int getSceneryPrice(String scenery_id){
        int sceneryPrice = 0;

        try {

            String sceneryCoinRequiredSql = """
                    SELECT PRICE FROM SCENERY WHERE SCENERY_ID = ?
                    """;

            sceneryPrice = jdbcTemplate.queryForObject(sceneryCoinRequiredSql, Integer.class, scenery_id);

        } catch (Exception e) {
            log.error("Error at ShopPageRepository getSceneryPrice", e);
            throw new RuntimeException(e);
        }

        return sceneryPrice;
    }

    public boolean checkUserCoinSufficientScenery(ShopPageModel shopPageModel){

        try {
            int userCoin = getUserCoin(shopPageModel.getUser_id());
            int sceneryPrice = getSceneryPrice(shopPageModel.getScenery_id());

            if (sceneryPrice > userCoin){
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            log.error("Error at ShopPageRepository checkUserCoinSufficientScenery", e);
            throw new RuntimeException(e);
        }
    }

    public boolean checkUserCoinSufficientCharacter(ShopPageModel shopPageModel){

        try {
            int userCoin = getUserCoin(shopPageModel.getUser_id());
            int characterPrice = getCharacterPrice(shopPageModel.getCharacter_id());

            if (characterPrice > userCoin){
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            log.error("Error at ShopPageRepository checkUserCoinSufficientCharacter", e);
            throw new RuntimeException(e);
        }
    }

    public void updateUserRemainingCoinScenery(ShopPageModel shopPageModel){
        try {

            int userCoin = getUserCoin(shopPageModel.getUser_id());
            int sceneryPrice = getSceneryPrice(shopPageModel.getScenery_id());

            int userRemainingCoin = userCoin - sceneryPrice;

            String updateUserCoinSql = """
                    UPDATE "User"
                    SET coins = ?
                    WHERE user_id = ?
                    """;

            jdbcTemplate.update(updateUserCoinSql, userRemainingCoin, shopPageModel.getUser_id());

        } catch (Exception e) {
            log.error("Error at ShopPageRepository updateUserRemainingCoinScenery", e);
            throw new RuntimeException(e);
        }
    }

    public void updateUserRemainingCoinCharacter(ShopPageModel shopPageModel){
        try {

            int userCoin = getUserCoin(shopPageModel.getUser_id());
            int characterPrice = getCharacterPrice(shopPageModel.getCharacter_id());

            int userRemainingCoin = userCoin - characterPrice;

            String updateUserCoinSql = """
                    UPDATE "User"
                    SET coins = ?
                    WHERE user_id = ?
                    """;

            jdbcTemplate.update(updateUserCoinSql, userRemainingCoin, shopPageModel.getUser_id());

        } catch (Exception e) {
            log.error("Error at ShopPageRepository updateUserRemainingCoinCharacter", e);
            throw new RuntimeException(e);
        }
    }

    public boolean checkUserHaveTheCharacter(ShopPageModel shopPageModel){
        try {

            String checkUserHaveTheCharacterSql = """
                    SELECT COUNT(*) FROM usercharacter WHERE character_id = ? AND user_id = ?
                    """;

            // Kalau Return Ada Value Berati Karakternya User Sudah Punya
            int check = jdbcTemplate.queryForObject(
                    checkUserHaveTheCharacterSql,
                    Integer.class,
                    shopPageModel.getCharacter_id(),
                    shopPageModel.getUser_id()
            );

            if(check > 0){
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            log.error("Error at ShopPageRepository checkUserHaveTheCharacter", e);
            throw new RuntimeException(e);
        }
    }

    public boolean checkUserHaveTheScenery(ShopPageModel shopPageModel){
        try {

            String checkUserHaveTheScenerySql = """
                    SELECT COUNT(*) FROM userscenery WHERE scenery_id = ? AND user_id = ?
                    """;

            // Kalau Return Ada Value Berati Karakternya User Sudah Punya
            int check = jdbcTemplate.queryForObject(
                    checkUserHaveTheScenerySql,
                    Integer.class,
                    shopPageModel.getScenery_id(),
                    shopPageModel.getUser_id()
            );

            if(check > 0){
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            log.error("Error at ShopPageRepository checkUserHaveTheScenery", e);
            throw new RuntimeException(e);
        }
    }

}
