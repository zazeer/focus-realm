package id.co.focusrealm.backend.Log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@Slf4j
public class LogRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertLog(LogModel logModel){

        String sql = """
                INSERT INTO log\s
                (log_id, user_id, type, created_at)
                VALUES (?, ?, ?, ?)
                """;

        try {

            jdbcTemplate.update(sql,
                    logModel.getLog_id(),
                    logModel.getUser_id(),
                    logModel.getType(),
                    logModel.getCreated_at()
            );

        } catch (Exception e) {
            log.info("Error At logRepository insertLog");
            throw new RuntimeException(e);
        }

    }

    public String getLastLogId(){
        String lastLogId = null;

        try {

            String sql = """
                    SELECT log_id FROM log ORDER BY log_id DESC LIMIT 1;
                    """;

            lastLogId = jdbcTemplate.queryForObject(sql, String.class);

        } catch (Exception e) {
            log.error("Error At LogRepository getLastLogId");
            throw new RuntimeException(e);
        }

        return lastLogId;
    }

    public boolean checkHasValue(){
        boolean check = false;

        try {

            String checkHasValueSql = """
                    SELECT COUNT(*) FROM log
            """;

            int hasValue =  jdbcTemplate.queryForObject(checkHasValueSql, Integer.class);

            if(hasValue > 0){
                check = true;
            } else {
                check = false;
            }

        } catch (Exception e) {
            log.error("Error At logRepository checkHasValue");
            throw new RuntimeException(e);
        }

        return check;
    }

}
