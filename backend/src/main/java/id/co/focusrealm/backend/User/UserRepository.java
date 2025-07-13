package id.co.focusrealm.backend.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    RowMapper<UserModel> userRowMapper = ((rs, rowNum) -> {
        UserModel user = new UserModel();
        user.setUser_Id(rs.getString("user_id"));
        user.setMusic_Id(rs.getString("music_id"));
        user.setAmbient_Id(rs.getString("ambient_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setCoins(rs.getInt("coins"));
        user.setPity(rs.getInt("pity"));
        user.setCreated_at(rs.getTimestamp("created_at"));
        return user;
    });

    public void insertUser(UserModel user) {
        try {

            String insertUserSql = """
                    INSERT INTO "User" (
                    user_id, music_id, ambient_id,
                    username, email, password, coins, pity, created_at
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            jdbcTemplate.update(insertUserSql,
                    user.getUser_Id(),
                    user.getMusic_Id(),
                    user.getAmbient_Id(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getCoins(),
                    user.getPity(),
                    user.getCreated_at()
            );

        } catch (Exception e) {
            log.error("Error At UserRepository.insertUser");
            throw new RuntimeException(e);
        }
    }

    public String getLastUserId(){
        String lastUserId = null;

        try {

            String getLastUserIdSql = """
                    SELECT user_id FROM "User" ORDER BY user_id DESC LIMIT 1;
                    """;

            lastUserId = jdbcTemplate.queryForObject(getLastUserIdSql, String.class);

        } catch (Exception e) {
            log.error("Error At UserRepository.getLastUserId");
            throw new RuntimeException(e);
        }

        return lastUserId;
    }

    public boolean checkHasValue(){
        boolean check = false;

        try {

            String checkHasValueSql = """
                    SELECT COUNT(*) FROM "User"
            """;

            int hasValue =  jdbcTemplate.queryForObject(checkHasValueSql, Integer.class);

            if(hasValue > 0){
                check = true;
            } else {
                check = false;
            }

        } catch (Exception e) {
            log.error("Error At UserRepository.checkHasValue");
            throw new RuntimeException(e);
        }

        return check;
    }

    public int fetchUser(UserModel user){
        int notFoundCode = 1;
        int wrongPasswordCode = 2;
        int SuccessFetchCode = 3;

        try {

            if(checkHasValue() == false){
                return notFoundCode;
            } else if (checkUserNameExists(user.getUsername()) == false){
                return notFoundCode;
            } else if (checkPasswordCorrect(user.getUsername(), user.getPassword()) == false){
                return wrongPasswordCode;
            } else {
                String fetchUserSql = """
                        SELECT * FROM "User" WHERE username = ? AND  password = ?
                        """;

                UserModel result = jdbcTemplate.queryForObject(fetchUserSql, userRowMapper, user.getUsername(), user.getPassword());

                user.setUser_Id(result.getUser_Id());
                user.setUsername(result.getUsername());
                user.setPassword(result.getPassword());
                user.setEmail(result.getEmail());
                user.setAmbient_Id(result.getAmbient_Id());
                user.setMusic_Id(result.getMusic_Id());
                user.setPassword(result.getPassword());
                user.setCoins(result.getCoins());
                user.setPity(result.getPity());
                user.setCreated_at(result.getCreated_at());
            }

        } catch (Exception e) {
            log.error("Error At UserRepository.fetchUser");
            throw new RuntimeException(e);
        }

        return SuccessFetchCode;
    }

    public boolean checkUserNameExists(String userName){
        boolean check = false;
        try {

            String checkUserNameExistsSql = """
                        SELECT COUNT (username) FROM "User" WHERE username = ?;
                    """;

            int hasValue =  jdbcTemplate.queryForObject(checkUserNameExistsSql, Integer.class, userName);

            if(hasValue > 0){
                check = true;
            } else {
                check = false;
            }

        } catch (Exception e) {
            log.error("Error At UserRepository.checkUserNameExists");
            throw new RuntimeException(e);
        }
        return check;
    }

    public boolean checkPasswordCorrect(String userName, String password){
        boolean check = false;
        try {

            String  checkPasswordCorrectSql = """
                SELECT COUNT (username) FROM "User" WHERE username = ? AND  password = ?
            """;

            int hasValue =  jdbcTemplate.queryForObject(checkPasswordCorrectSql, Integer.class, userName, password);

            if(hasValue > 0){
                check = true;
            } else  {
                check = false;
            }

        } catch (Exception e) {
            log.error("Error At UserRepository.checkPasswordCorrect");
            throw new RuntimeException(e);
        }

        return check;
    }

    public boolean checkEmailTaken(String email){
        boolean check = false;

        try {
            String checkEmailTakenSql = """
                    SELECT COUNT (username) FROM "User" WHERE email = ?
                    """;

            int hasValue =  jdbcTemplate.queryForObject(checkEmailTakenSql, Integer.class, email);

            if(hasValue > 0){
                check = true;
            } else  {
                check = false;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return check;
    }

}
