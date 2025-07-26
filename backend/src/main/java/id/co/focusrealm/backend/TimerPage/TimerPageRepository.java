package id.co.focusrealm.backend.TimerPage;

import id.co.focusrealm.backend.HomePage.HomePageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class TimerPageRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    RowMapper<TimerPageModel> timerPageRowMapper = ((rs, rowNum) -> {
        TimerPageModel timerPageModel = new TimerPageModel();

        // User Information For TimerPage
        timerPageModel.setUser_id(rs.getString("user_id"));

        // Character Information For TimerPage
        timerPageModel.setCharacter_id(rs.getString("character_id"));
        timerPageModel.setCharacter_name(rs.getString("character_name"));
        timerPageModel.setCharacter_file_name(rs.getString("character_file_name"));

        // Scenery Information For TimerPage
        timerPageModel.setScenery_id(rs.getString("scenery_id"));
        timerPageModel.setScenery_name(rs.getString("scenery_name"));
        timerPageModel.setScenery_file_name(rs.getString("scenery_file_name"));

        // Ambient Information For TimerPage
        timerPageModel.setAmbient_id(rs.getString("ambient_id"));
        timerPageModel.setAmbient_title(rs.getString("ambient_title"));
        timerPageModel.setAmbient_file_name(rs.getString("ambient_file_name"));

        // Music Information For TimerPage
        timerPageModel.setMusic_id(rs.getString("music_id"));
        timerPageModel.setMusic_title(rs.getString("music_title"));
        timerPageModel.setMusic_file_name(rs.getString("music_file_name"));
        timerPageModel.setMusic_duration(rs.getInt("duration"));

        return timerPageModel;
    });

    public void fetchTimerPageDataByUserId(TimerPageModel timerPageModel){

        try {

            String fetchDataSql = """
                SELECT "User".user_id,
                        Character.character_id, Character.character_name, Character.file_name AS "character_file_name",
                        Scenery.scenery_id, Scenery.scenery_name, Scenery.file_name AS "scenery_file_name",
                        music.music_id, music.title AS "music_title", music.file_name AS "music_file_name", music.duration,
                        Ambient.ambient_id, Ambient.title AS "ambient_title", Ambient.file_name AS "ambient_file_name"
                    
                        FROM "User" JOIN UserCharacter ON "User".user_id = UserCharacter.user_id
                        JOIN Character ON UserCharacter.character_id = Character.character_id
                        JOIN UserScenery ON "User".user_id = UserScenery.user_id
                        JOIN Scenery ON UserScenery.scenery_id = Scenery.scenery_id
                        JOIN Music ON "User".music_id = Music.music_id
                        JOIN Ambient ON "User".ambient_id = Ambient.ambient_id
                        WHERE UserScenery.chosen_scenery = True AND UserCharacter.chosen_character = True AND "User".user_id = ?
            """;

            TimerPageModel result = jdbcTemplate.queryForObject(fetchDataSql, timerPageRowMapper, timerPageModel.getUser_id());

            timerPageModel.setUser_id(result.getUser_id());

            timerPageModel.setScenery_id(result.getScenery_id());
            timerPageModel.setScenery_file_name(result.getScenery_file_name());
            timerPageModel.setScenery_name(result.getScenery_name());

            timerPageModel.setAmbient_id(result.getAmbient_id());
            timerPageModel.setAmbient_title(result.getAmbient_title());
            timerPageModel.setAmbient_file_name(result.getAmbient_file_name());

            timerPageModel.setMusic_id(result.getMusic_id());
            timerPageModel.setMusic_title(result.getMusic_title());
            timerPageModel.setMusic_file_name(result.getMusic_file_name());
            timerPageModel.setMusic_duration(result.getMusic_duration());

            timerPageModel.setCharacter_id(result.getCharacter_id());
            timerPageModel.setCharacter_name(result.getCharacter_name());
            timerPageModel.setCharacter_file_name(result.getCharacter_file_name());

        } catch (Exception e) {
            log.error("Error at TimerPageRepository.fetchTimerPageDataByUserId", e);
            throw new RuntimeException(e);
        }

    }

}
