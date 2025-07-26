package id.co.focusrealm.backend.FocusSession;

import id.co.focusrealm.backend.UserCharacter.UserCharacterModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class FocusSessionRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void insertFocusSession(FocusSessionModel focusSessionModel){

        try {

            String insertUserCharacterSql = """
                    INSERT INTO "focussession" (
                    	focus_session_id, user_id, analytics_id, total_focus_duration, total_break_duration, interval, total_coins_made
                    ) VALUES (?, ?, ?, ?, ?, ?, ?)
                    """;

            jdbcTemplate.update(insertUserCharacterSql,
                    focusSessionModel.getFocus_session_id(),
                    focusSessionModel.getUser_id(),
                    focusSessionModel.getAnalytics_id(),
                    focusSessionModel.getTotal_focus_duration(),
                    focusSessionModel.getTotal_break_duration(),
                    focusSessionModel.getInterval(),
                    focusSessionModel.getTotal_coins_made()
            );

        } catch (Exception e) {
            log.info("Error in FocusSessionRepository insertFocusSession");
            throw new RuntimeException(e);
        }

    }

    public boolean checkHasValue(){
        boolean check = false;

        try {

            String checkHasValueSql = """
                    SELECT COUNT(*) FROM focussession
            """;

            int hasValue =  jdbcTemplate.queryForObject(checkHasValueSql, Integer.class);

            if(hasValue > 0){
                check = true;
            } else {
                check = false;
            }

        } catch (Exception e) {
            log.error("Error At FocusSessionRepository checkHasValue");
            throw new RuntimeException(e);
        }

        return check;
    }

    public String getLastFocusSessionId(){
        String lastFocusSessionId = null;

        try {

            String getLastFocusSessionSql = """
                        SELECT focus_session_id FROM focussession ORDER BY focus_session_id DESC LIMIT 1;
                    """;

            lastFocusSessionId = jdbcTemplate.queryForObject(getLastFocusSessionSql, String.class);

        } catch (Exception e) {
            log.error("Error At FocusSessionRepository getLastFocusSessionId");
            throw new RuntimeException(e);
        }

        return lastFocusSessionId;
    }

}
