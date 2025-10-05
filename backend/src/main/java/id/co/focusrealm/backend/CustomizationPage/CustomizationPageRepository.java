package id.co.focusrealm.backend.CustomizationPage;

import id.co.focusrealm.backend.CharacterSceneryModelPackage.ObtainedCharacterModel;
import id.co.focusrealm.backend.CharacterSceneryModelPackage.ObtainedSceneryModel;
import id.co.focusrealm.backend.CharacterSceneryModelPackage.UnobtainedCharacterModel;
import id.co.focusrealm.backend.CharacterSceneryModelPackage.UnobtainedSceneryModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Slf4j
@Repository
public class CustomizationPageRepository {

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
            log.error("Error at CustomizationPageRepository getUnobtainedCharacter", e);
            throw new RuntimeException(e);
        }

        return characterModel;
    }

    public ArrayList<ObtainedCharacterModel> getObtainedCharacterExcludingCurrentlyUsed(String user_id) {

        String sql = "SELECT c.character_id, c.character_name, c.character_rarity, c.price, c.release_date, c.file_name, c.character_description, uc.acquire_date, uc.chosen_character\n" +
                "FROM character c \n" +
                "JOIN usercharacter uc ON c.character_id = uc.character_id\n" +
                "WHERE user_id = ? AND uc.chosen_character = false ";
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
            log.error("Error at CustomizationPageRepository getObtainedCharacterExcludingCurrentlyUsed", e);
            throw new RuntimeException(e);
        }

        return characterModel;
    }

    public ObtainedCharacterModel getCurrentlyUsedCharacter(String user_id) {

        String sql = "SELECT c.character_id, c.character_name, c.character_rarity, c.price, c.release_date, c.file_name, c.character_description, uc.acquire_date, uc.chosen_character\n" +
                "FROM character c \n" +
                "JOIN usercharacter uc ON c.character_id = uc.character_id\n" +
                "WHERE user_id = ? AND uc.chosen_character = true ";
        ObtainedCharacterModel characterModel = new ObtainedCharacterModel();

        try {
            characterModel = jdbcTemplate.queryForObject(sql, new Object[]{user_id},(rs, rowNum) -> {
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
                return character;
            });

        } catch (Exception e) {
            log.error("Error at CustomizationPageRepository getCurrentlyUsedCharacter", e);
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
            log.error("Error at CustomizationPageRepository getUnobtainedScenery", e);
            throw new RuntimeException(e);
        }

        return unobtainedScenery;
    }

    public ArrayList<ObtainedSceneryModel> getObtainedSceneryExcludingCurrentlyUsed(String user_id) {

        String sql = "SELECT s.scenery_id, s.scenery_name, s.scenery_rarity, s.price, s.file_name, s.release_date, s.scenery_description, us.acquire_date, us.chosen_scenery \n" +
                "FROM scenery s JOIN userscenery us ON us.scenery_id = s.scenery_id\n" +
                "WHERE user_id = ? AND us.chosen_scenery = false";
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
            log.error("Error at CustomizationPageRepository getObtainedSceneryExcludingCurrentlyUsed", e);
            throw new RuntimeException(e);
        }

        return obtainedScenery;
    }

    public ObtainedSceneryModel getCurrentlyUsedScenery(String user_id) {

        String sql = "SELECT s.scenery_id, s.scenery_name, s.scenery_rarity, s.price, s.file_name, s.release_date, s.scenery_description, us.acquire_date, us.chosen_scenery \n" +
                "FROM scenery s JOIN userscenery us ON us.scenery_id = s.scenery_id\n" +
                "WHERE user_id = ? AND us.chosen_scenery = true";
        ObtainedSceneryModel obtainedScenery = new ObtainedSceneryModel();

        try {
            obtainedScenery = jdbcTemplate.queryForObject(sql, new Object[]{user_id},(rs, rowNum) -> {
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
                return scenery;
            });

        } catch (Exception e) {
            log.error("Error at CustomizationPageRepository getCurrentlyUsedScenery", e);
            throw new RuntimeException(e);
        }

        return obtainedScenery;
    }

    public void changeChosenCharacter(CustomizationPageModel customizationPageModel){

        try {

            String removeChosenCharacterSql = "UPDATE usercharacter \n" +
                    "SET chosen_character = false WHERE user_id = ? AND chosen_character = true";

                    String addChosenCharacterSql = "UPDATE usercharacter\n" +
                    "SET chosen_character = true\n" +
                    "WHERE user_id = ?\n" +
                    "AND character_id = ?";

            int removed = jdbcTemplate.update(removeChosenCharacterSql, customizationPageModel.getUser_id());
            int added = jdbcTemplate.update(addChosenCharacterSql, customizationPageModel.getUser_id(), customizationPageModel.getNewUsedCharacterId());

            log.info("REMOVED CHOSEN CHARACTER :" + removed);
            log.info("ADDED CHOSEN CHARACTER :" + added);

        } catch (Exception e) {
            log.error("Error at CustomizationPageRepository changeChosenCharacter", e);
            throw new RuntimeException(e);
        }
    }

    public void changeChosenScenery(CustomizationPageModel customizationPageModel){

        try {

            String removeChosenScenerySql = "UPDATE userscenery \n" +
                    "SET chosen_scenery = false WHERE user_id = ? AND chosen_scenery = true";

            String addChosenScenerySql = "UPDATE userscenery\n" +
                    "SET chosen_scenery = true\n" +
                    "WHERE user_id = ?\n" +
                    "AND scenery_id = ?";

            int removed = jdbcTemplate.update(removeChosenScenerySql, customizationPageModel.getUser_id());
            int added = jdbcTemplate.update(addChosenScenerySql, customizationPageModel.getUser_id(), customizationPageModel.getNewUsedSceneryId());

            log.info("REMOVED CHOSEN SCENERY :" + removed);
            log.info("ADDED CHOSEN SCENERY :" + added);

        } catch (Exception e) {
            log.error("Error at CustomizationPageRepository changeChosenScenery", e);
            throw new RuntimeException(e);
        }

    }

    public void updateUserSettings(CustomizationPageModel customizationPageModel){

        try {

            changeChosenCharacter(customizationPageModel);
            changeChosenScenery(customizationPageModel);

        } catch (Exception e) {
            log.error("Error at CustomizationPageRepository updateUserSettings", e);
            throw new RuntimeException(e);
        }

    }

}
