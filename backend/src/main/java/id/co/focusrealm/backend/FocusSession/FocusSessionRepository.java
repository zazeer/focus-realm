package id.co.focusrealm.backend.FocusSession;

import id.co.focusrealm.backend.CharacterSceneryModelPackage.UnobtainedCharacterModel;
import id.co.focusrealm.backend.ShopPage.ShopPageModel;
import id.co.focusrealm.backend.UserCharacter.UserCharacterModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@Slf4j
public class FocusSessionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertFocusSession(FocusSessionModel focusSessionModel){

        try {

            String insertUserCharacterSql = """
                    INSERT INTO "focussession" (
                    	focus_session_id, user_id, analytics_id, total_focus_duration, total_break_duration, interval, total_coins_made, session_date
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            jdbcTemplate.update(insertUserCharacterSql,
                    focusSessionModel.getFocus_session_id(),
                    focusSessionModel.getUser_id(),
                    focusSessionModel.getAnalytics_id(),
                    focusSessionModel.getTotal_focus_duration(),
                    focusSessionModel.getTotal_break_duration(),
                    focusSessionModel.getInterval(),
                    focusSessionModel.getTotal_coins_made(),
                    focusSessionModel.getSession_date()
            );

        } catch (Exception e) {
            log.info("Error in FocusSessionRepository insertFocusSession");
            throw new RuntimeException(e);
        }

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

    public void updateUserCoins(FocusSessionModel focusSessionModel){
        try {

            int userCoin = getUserCoin(focusSessionModel.getUser_id());
            int userCoinsGained = userCoin + focusSessionModel.getTotal_coins_made();

            String updateUserCoinSql = """
                    UPDATE "User"
                    SET coins = ?
                    WHERE user_id = ?
                    """;

            jdbcTemplate.update(updateUserCoinSql, userCoinsGained, focusSessionModel.getUser_id());

        } catch (Exception e) {
            log.error("Error at FocusSessionRepository updateUserCoins", e);
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

    public ArrayList<FocusSessionModel> fetchAllFocusSession(String user_id, String analytics_id){

        try {

            String fetchAllFocusSessionByUserIdSql = "SELECT * FROM focussession \n" +
                    "where user_id = ? AND analytics_id = ?";

            ArrayList<FocusSessionModel> focusSessionModels = new ArrayList<>();

            jdbcTemplate.query(fetchAllFocusSessionByUserIdSql, new Object[]{user_id, analytics_id},(rs, rowNum) -> {
                FocusSessionModel focusSessionModel = new FocusSessionModel();
                focusSessionModel.setFocus_session_id(rs.getString("focus_session_id"));
                focusSessionModel.setUser_id(rs.getString("user_id"));
                focusSessionModel.setAnalytics_id(rs.getString("analytics_id"));
                focusSessionModel.setTotal_focus_duration(rs.getInt("total_focus_duration"));
                focusSessionModel.setTotal_break_duration(rs.getInt("total_break_duration"));
                focusSessionModel.setInterval(rs.getInt("interval"));
                focusSessionModel.setTotal_coins_made(rs.getInt("total_coins_made"));
                focusSessionModel.setSession_date(rs.getDate("session_date"));
                focusSessionModels.add(focusSessionModel);
                return null;
            });
            return focusSessionModels;

        } catch (Exception e) {
            log.error("FocusSessionRepository fetchAllFocusSessionByUserId");
            throw new RuntimeException(e);
        }

    }

}
