package id.co.focusrealm.backend.HomePage;

import id.co.focusrealm.backend.User.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class HomePageRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    RowMapper<HomePageModel> homePageRowMapper = ((rs, rowNum) -> {
        HomePageModel homePageModel = new HomePageModel();

        // User Information For HomePage
        homePageModel.setUser_id(rs.getString("user_id"));
        homePageModel.setUsername(rs.getString("username"));
        homePageModel.setUser_coins(rs.getInt("coins"));

        // Character Information For HomePage
        homePageModel.setCharacter_id(rs.getString("character_id"));
        homePageModel.setCharacter_name(rs.getString("character_name"));
        homePageModel.setCharacter_file_name(rs.getString("character_file_name"));

        // Scenery Information For Homepage
        homePageModel.setScenery_id(rs.getString("scenery_id"));
        homePageModel.setScenery_name(rs.getString("scenery_name"));
        homePageModel.setScenery_file_name(rs.getString("scenery_file_name"));

        return homePageModel;
    });

    public HomePageModel fetchHomePageDataByUserId(HomePageModel homePageModel){
        try {

            String fetchDataSql = """ 
                        SELECT "User".user_id, "User".username, "User".coins,
                        Character.character_id, Character.character_name, Character.file_name AS "character_file_name",
                        Scenery.scenery_id, Scenery.scenery_name, Scenery.file_name AS "scenery_file_name"
                        
                        FROM "User" JOIN UserCharacter ON "User".user_id = UserCharacter.user_id
                        JOIN Character ON UserCharacter.character_id = Character.character_id
                        JOIN UserScenery ON "User".user_id = UserScenery.user_id
                        JOIN Scenery ON UserScenery.scenery_id = Scenery.scenery_id
                        WHERE UserScenery.chosen_scenery = True AND UserCharacter.chosen_character = True AND "User".user_id = ?
                    """;

            HomePageModel result = jdbcTemplate.queryForObject(fetchDataSql, homePageRowMapper, homePageModel.getUser_id());

            return result;

        } catch (Exception e) {
            log.error("Error At HomePageRepository.fetchHomePageDataByUserId");
            throw new RuntimeException(e);
        }
    }

}
