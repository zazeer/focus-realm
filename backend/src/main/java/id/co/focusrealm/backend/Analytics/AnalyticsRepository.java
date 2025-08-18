package id.co.focusrealm.backend.Analytics;

import id.co.focusrealm.backend.UserCharacter.UserCharacterModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class AnalyticsRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void insertAnalytics(AnalyticsModel analyticsModel){

        try {

            String insertAnalyticsSql = """
                    INSERT INTO "analytics" (
                    	analytics_id, user_id, daily_focus_duration, daily_total_session, daily_coins_made, longest_daily_streak
                    ) VALUES (?, ?, ?, ?, ?, ?)
                    """;

            jdbcTemplate.update(insertAnalyticsSql,
                    analyticsModel.getAnalytics_id(),
                    analyticsModel.getUser_id(),
                    analyticsModel.getDaily_focus_duration(),
                    analyticsModel.getDaily_total_session(),
                    analyticsModel.getDaily_coins_made(),
                    analyticsModel.getLongest_daily_streak()
            );

        } catch (Exception e) {
            log.info("Error in AnalyticsRepository insertAnalytics");
            throw new RuntimeException(e);
        }

    }

    public boolean checkHasValue(){
        boolean check = false;

        try {

            String checkHasValueSql = """
                    SELECT COUNT(*) FROM analytics
            """;

            int hasValue =  jdbcTemplate.queryForObject(checkHasValueSql, Integer.class);

            if(hasValue > 0){
                check = true;
            } else {
                check = false;
            }

        } catch (Exception e) {
            log.error("Error At AnalyticsRepository checkHasValue");
            throw new RuntimeException(e);
        }

        return check;
    }

    public String getLastAnalyticsId(){
        String lastAnalyticsId = null;

        try {

            String getLastUserIdSql = """
                        SELECT analytics_id FROM analytics ORDER BY analytics_id DESC LIMIT 1;
                    """;

            lastAnalyticsId = jdbcTemplate.queryForObject(getLastUserIdSql, String.class);

        } catch (Exception e) {
            log.error("Error At AnalyticsRepository getLastAnalyticsId");
            throw new RuntimeException(e);
        }

        return lastAnalyticsId;
    }


    public String getAnalyticsIdByUserId(String userId){
        String analyticsId = null;

        try {

            String AnalyticsIdByUserIdSql = """
                        SELECT analytics_id FROM analytics where user_id = ? 
                    """;

            analyticsId = jdbcTemplate.queryForObject(AnalyticsIdByUserIdSql, String.class, userId);

        } catch (Exception e) {
            log.error("Error at AnalyticsService getAnalyticsIdByUserId", e);
            throw new RuntimeException(e);
        }

        return analyticsId;
    }

}
