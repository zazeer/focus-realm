package id.co.focusrealm.backend.GachaPage;

import id.co.focusrealm.backend.Character.CharacterModel;
import id.co.focusrealm.backend.CharacterSceneryModelPackage.ObtainedCharacterModel;
import id.co.focusrealm.backend.CharacterSceneryModelPackage.UnobtainedCharacterModel;
import id.co.focusrealm.backend.HomePage.HomePageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@Slf4j
public class GachaRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    RowMapper<GachaModel> gatchaPageRowMapper = ((rs, rowNum) -> {
        GachaModel gachaModel = new GachaModel();

        // User Information For HomePage
        gachaModel.setUser_pity(rs.getInt("pity"));
        gachaModel.setUser_coins(rs.getInt("coins"));

        return gachaModel;
    });

    public void fetchUserDataGachaPage(GachaModel gachaModel){

        String sql = """
                SELECT coins, pity FROM "User" WHERE user_id = '?'
                """;

        try {

            GachaModel result = jdbcTemplate.queryForObject(sql, gatchaPageRowMapper, gachaModel.getUser_id());

            gachaModel.setUser_pity(result.getUser_pity());
            gachaModel.setUser_coins(result.getUser_coins());

        } catch (Exception e) {
            log.error("Error At GachaRepository fetchUserDataGachaPage");
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

}
