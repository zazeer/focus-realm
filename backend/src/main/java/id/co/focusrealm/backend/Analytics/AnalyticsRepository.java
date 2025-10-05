package id.co.focusrealm.backend.Analytics;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class AnalyticsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertAnalytics(AnalyticsModel analyticsModel){

        try {

            String insertAnalyticsSql = """
                    INSERT INTO "analytics" (
                    	analytics_id, user_id, total_focus_duration, total_focus_session, total_coins_made
                    ) VALUES (?, ?, ?, ?, ?)
                    """;

            jdbcTemplate.update(insertAnalyticsSql,
                    analyticsModel.getAnalytics_id(),
                    analyticsModel.getUser_id(),
                    analyticsModel.getTotal_focus_duration(),
                    analyticsModel.getTotal_focus_session(),
                    analyticsModel.getTotal_coins_made()
            );

        } catch (Exception e) {
            log.info("Error in AnalyticsRepository insertAnalytics");
            throw new RuntimeException(e);
        }

    }

    public void updateAnalytics(AnalyticsModel analyticsModel){

        try {

            String updateAnalyticsSql = "UPDATE analytics\n" +
                    "SET total_focus_duration = total_focus_duration + ?,\n" +
                    "total_focus_session = total_focus_session + 1, \n" +
                    "total_coins_made = total_coins_made + ? \n" +
                    "WHERE user_id = ?";

            jdbcTemplate.update(updateAnalyticsSql,
                    analyticsModel.getTotal_focus_duration(),
                    analyticsModel.getTotal_coins_made(),
                    analyticsModel.getUser_id()
            );

        } catch (Exception e) {
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

    public AnalyticsModel fetchAnalyticsDataByUserId(String userId){
        try {

            String fetchAnalyticsDataByUserIdSql = "SELECT analytics_id, user_id, total_focus_duration, total_focus_session, total_coins_made\n" +
                    "FROM analytics\n" +
                    "WHERE user_id = ?";

            AnalyticsModel analyticsModel = jdbcTemplate.queryForObject(fetchAnalyticsDataByUserIdSql, new Object[]{userId},
                    (rs, rowNum) -> {
                        AnalyticsModel temp = new AnalyticsModel();
                        temp.setAnalytics_id(rs.getString("analytics_id"));
                        temp.setUser_id(rs.getString("user_id"));
                        temp.setTotal_focus_duration(rs.getInt("total_focus_duration"));
                        temp.setTotal_focus_session(rs.getInt("total_focus_session"));
                        temp.setTotal_coins_made(rs.getInt("total_coins_made"));
                        return temp;
                    }
            );

            return analyticsModel;

        } catch (Exception e) {
            log.error("Error at AnalyticsService fetchAnalyticsDataByUserId", e);
            throw new RuntimeException(e);
        }
    }

}
