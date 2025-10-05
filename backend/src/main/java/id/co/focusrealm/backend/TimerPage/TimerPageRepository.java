package id.co.focusrealm.backend.TimerPage;

import id.co.focusrealm.backend.TimerPage.Models.AmbientModel;
import id.co.focusrealm.backend.TimerPage.Models.MusicModel;
import id.co.focusrealm.backend.TimerPage.Models.TimerPageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

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
        AmbientModel ambientModel = new AmbientModel();
        ambientModel.setAmbient_id(rs.getString("ambient_id"));
        ambientModel.setAmbient_title(rs.getString("ambient_title"));
        ambientModel.setAmbient_file_name(rs.getString("ambient_file_name"));
        timerPageModel.setCurrentAmbientModel(ambientModel);

        // Music Information For TimerPage
        MusicModel musicModel = new MusicModel();
        musicModel.setMusic_id(rs.getString("music_id"));
        musicModel.setMusic_title(rs.getString("music_title"));
        musicModel.setMusic_file_name(rs.getString("music_file_name"));
        musicModel.setMusic_duration(rs.getInt("duration"));
        timerPageModel.setCurrentMusicModel(musicModel);

        return timerPageModel;
    });

    public TimerPageModel fetchTimerPageDataByUserId(TimerPageModel timerPageModel){

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

            return result;

        } catch (Exception e) {
            log.error("Error at TimerPageRepository.fetchTimerPageDataByUserId", e);
            throw new RuntimeException(e);
        }

    }

    public ArrayList<MusicModel> fetchAllMusic(TimerPageModel timerPageModel){

        String sql = "SELECT * FROM MUSIC";
        ArrayList<MusicModel> allMusicList = new ArrayList<MusicModel>();

        try {

            jdbcTemplate.query(sql, (rs, rowNum) -> {
                MusicModel musicModelTemp = new MusicModel();
                musicModelTemp.setMusic_id(rs.getString("music_id"));
                musicModelTemp.setMusic_title(rs.getString("title"));
                musicModelTemp.setMusic_file_name(rs.getString("file_name"));
                musicModelTemp.setMusic_duration(rs.getInt("duration"));
                allMusicList.add(musicModelTemp);
                return null;
            });

            return allMusicList;

        } catch (Exception e) {
            log.error("Error at TimerPageRepository FetchAllMusic");
            throw new RuntimeException(e);
        }
    }

    public ArrayList<AmbientModel> fetchAllAmbient(TimerPageModel timerPageModel){

        String sql = "SELECT * FROM AMBIENT";
        ArrayList<AmbientModel> allAmbientList = new ArrayList<AmbientModel>();

        try {

            jdbcTemplate.query(sql, (rs, rowNum) -> {
                AmbientModel ambientModelTemp = new AmbientModel();
                ambientModelTemp.setAmbient_id(rs.getString("ambient_id"));
                ambientModelTemp.setAmbient_title(rs.getString("title"));
                ambientModelTemp.setAmbient_file_name(rs.getString("file_name"));
                allAmbientList.add(ambientModelTemp);
                return null;
            });

            return allAmbientList;

        } catch (Exception e) {
            log.error("Error at TimerPageRepository FetchAllMusic");
            throw new RuntimeException(e);
        }
    }

}
